const dbManager = require('../db/dbManager');
const { exec } = require("child_process");
const fs = require("fs");
const path = require("path");
const { v4: uuidv4 } = require('uuid');
const archiver = require('archiver');
const youtubedl = require('youtube-dl')


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
        return res.status(404).send("A valid spotify URL hasn't been provided, please pass it in the request body");


    if (!fs.existsSync(path.join(__dirname, '..', 'spotify-downloads')))
        fs.mkdirSync(path.join(__dirname, '..') + '/spotify-downloads')


    const randValue = uuidv4();
    const directoryPathUnique = path.join(__dirname, '..', 'spotify-downloads') + '/' + randValue;
    fs.mkdirSync(directoryPathUnique)
    const directoryPath = directoryPathUnique + '/downloads';
    fs.mkdirSync(directoryPath)

    exec(`spotifydl ${spotifyURL} -o ${directoryPath}`, (error) => {
        if (error) {
            console.log(error)
            return res.status(404).send("Something went wrong, make sure the spotify URL is correct");
        }

        saveAsZipAndSendRes(directoryPathUnique, directoryPath, fs.readdirSync(directoryPath)[0], res);
    });
}

exports.youtube = async (req, res) => {
    res.set('zip-file-name', 'ciaooooo', 'what');
    let youtubeURL = req.body.youtubeURL;
    const audioOnly = req.body.audioOnly;

    if(!youtubeURL)
        return res.status(404).send("A valid youtube URL hasn't been provided, please pass it in the request body");


    if (!fs.existsSync(path.join(__dirname, '..', 'youtube-downloads')))
        fs.mkdirSync(path.join(__dirname, '..') + '/youtube-downloads')


    const randValue = uuidv4();
    const directoryPathUnique = path.join(__dirname, '..', 'youtube-downloads') + '/' + randValue;
    fs.mkdirSync(directoryPathUnique);

    const directoryPath = directoryPathUnique + '/downloads';
    fs.mkdirSync(directoryPath);

    exec(`youtube-dl -o "${directoryPath+'/%(title)s.%(ext)s'}" --no-playlist ${audioOnly ? '-f bestaudio[ext=m4a]' : ''} ${youtubeURL}`, (error) => {
        if (error) {
            console.log(error)
            return res.status(404).send("Something went wrong, make sure the youtube URL is correct");
        }
        else
            saveAsZipAndSendRes(directoryPathUnique, directoryPath, fs.readdirSync(directoryPath)[0], res);
    });
}

function saveAsZipAndSendRes(directoryPathUnique, directoryPath, zipName, res) {
    let newZipName = zipName.replace(path.extname(zipName), '');
    newZipName = newZipName.replace(/[^\x00-\x7F]/g, "");
    res.set('zip-file-name', newZipName);

    const archive = archiver('zip', { zlib: { level: 9 } });

    archive.on('end', () => {
        fs.rmdirSync(directoryPathUnique, { recursive: true });
    });

    archive.directory(`${directoryPath}/`, zipName);
    archive.pipe(res);
    archive.finalize();
}