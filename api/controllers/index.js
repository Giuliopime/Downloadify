const dbManager = require('../db/dbManager');
const { exec } = require("child_process");
const fs = require("fs");
const path = require("path");
const { v4: uuidv4 } = require('uuid');
const archiver = require('archiver');


exports.login = async (req, res) => {
    const token = req.headers.authorization;
    const user = await dbManager.getUser(token);

    if(!user) {
        res.status(401)
            .send("Token not valid");
    } else {
        res.cookie('token', user.token, { httpOnly: true });
        res.status(200)
            .json({username: user.username})
    }
};

exports.getUser = async (req, res) => {
    const token = req.cookies ? req.cookies.token : null;

    if(!token) {
        res.status(401)
            .send("Token not found");
    } else {
        const user = await dbManager.getUser(token);
        if(!user) {
            res.status(401)
                .send("Token not valid");
        } else {
            res.status(200)
                .json({username: user.username})
        }
    }
}

exports.newUser = async (req, res) => {
    if(await dbManager.newUser(req.body.token, req.body.admin === "true"))
        res.status(200).send("User created successfully");
    else
        res.status(500).send("The server encountered an error while creating the user in the database");
}


exports.spotify = async (req, res) => {
    const spotifyURL = req.body.spotifyURL;

    if(!spotifyURL)
        return res.status(404).send("A valid spotify URL hasn't been provided, please pass it in the request data using JSON");


    if (!fs.existsSync(path.join(__dirname, '..', 'spotify-downloads')))
        fs.mkdirSync(path.join(__dirname, '..') + '/spotify-downloads')


    const randValue = uuidv4();
    const directoryPathUnique = path.join(__dirname, '..', 'spotify-downloads') + '/' + randValue;
    fs.mkdirSync(directoryPathUnique)
    const directoryPath = directoryPathUnique + '/downloads';
    fs.mkdirSync(directoryPath)

    exec(`spotifydl ${spotifyURL} -o ${directoryPath}`, (error, stdout, stderr) => {
        if (error) {
            console.log(error)
            return res.status(404).send("Something went wrong, make sure the spotify URL is correct");
        }
        else {
            const name = fs.readdirSync(directoryPath)[0];
            res.set('zip-file-name', name.replace(path.extname(name), ''));

            const archive = archiver('zip', { zlib: { level: 9 } });

            archive.directory(`${directoryPath}/`, name);
            archive.pipe(res);
            archive.finalize();
        }
    });
}