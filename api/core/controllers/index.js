const { exec } = require("child_process");
const fs = require("fs");
const path = require("path");
const archiver = require('archiver');
const dbManager = require('../db/dbManager');
const downloadsHandler = require('../utils/downloadsHandler');

// Logs in the user and saves the token in a cookie (for the website), use /user if you want to use the APIs manually instead
exports.login = async (req, res) => {
    try {
        // Login can only be made via authorization header
        if(res.locals.authViaCookie) {
            return res.status(401)
                .send("Token not found, pass it in the Authorization header");
        }

        const user = res.locals.user;

        res.cookie('token', user.token, { httpOnly: true });
        res.status(200)
            .json({administrator: user.administrator});

    } catch (e) {
        handleError(res, e);
    }
};

// Gets user object (without token)
exports.getUser = async (req, res) => {
    try {
        const user = res.locals.user;

        res.status(200)
            .json({administrator: user.administrator});

    } catch (e) {
        handleError(res, e);
    }
}


exports.newUser = async (req, res) => {
    try {
        const user = res.locals.user;

        // Check if user has permissions to create new users
        if(!user.administrator)
            return res.status(403)
                .send("You don't have permissions to create new users");


        const newUser = await dbManager.newUser(req.body.token, req.body.admin === "true");
        if(newUser)
            res.status(200).json(newUser);
        else
            res.status(500).send("The server encountered an error while creating the user in the database, it's possible that the token wasn't unique.");

    } catch (e) {
        handleError(res, e);
    }
}



/*
FOLLOWING CONTROLLERS REQUIRE FFMPEG
 */

/*
REQUIRES SPOTDL NPM PACKAGE INSTALLED GLOBALLY (npm i -g spotify-dl)
 */
exports.spotify = async (req, res) => {
    try {
        const spotifyURL = req.body.spotifyURL;

        if(!spotifyURL)
            return res.status(404).send("A valid spotify URL hasn't been provided, please pass it in the request body");

        const downloadID = downloadsHandler.newDownload(spotifyURL, null);

        res.status(202).json({ downloadID: downloadID });

    } catch (e) {
        handleError(res, e);
    }
}


/*
REQUIRES YOUTUBE-DL (https://youtube-dl.org/)
 */
exports.youtube = async (req, res) => {
    try {
        let youtubeURL = req.body.youtubeURL;
        const audioOnly = req.body.audioOnly;

        if(!youtubeURL)
            return res.status(404).send("A valid youtube URL hasn't been provided, please pass it in the request body");

        const { directoryPathUnique, directoryPath } = setupDirectoriesForDownload('youtube-downloads');

        /*
        Run youtube-dl command
        -o is the output
        --no-playlist makes it so if a yt link includes a playlist (other than a video) it downloads only the video and not the full playlist
        -f bestaudio[ext=m4a] downloads only the audio, in m4a format
         */
        exec(`youtube-dl -o "${directoryPath+'/%(title)s.%(ext)s'}" --no-playlist ${audioOnly ? '-f bestaudio[ext=m4a]' : ''} ${youtubeURL}`, (error) => {
            if (error) {
                console.log(error)
                return res.status(404).send("Something went wrong, make sure the youtube URL is correct");
            }
            else
                saveAsZipAndSendRes(directoryPathUnique, directoryPath, fs.readdirSync(directoryPath)[0], res);
        });
    } catch (e) {
        handleError(res, e);
    }
}

exports.downloadStatus = (req, res) => {
    try {
        res.status(202).json(downloadsHandler.getDownloadStatus(req.params.id));
    } catch (e) {
        handleError(res, e);
    }
}

exports.getDownloadedData = (req, res) => {
    try {
        const downloadData = downloadsHandler.getDownloadStatus(req.params.id);
        if(!downloadData.finished)
            return res.status(404).send('Download data is not yet available');

        sendZip(downloadData, res);
    } catch (e) {
        handleError(res, e);
    }
}

const handleError = (res, e) => {
    console.log(e);
    res.status(500)
        .send(`Something bad happened ¯\_(ツ)_/¯`);
}

const sendZip = (downloadData, res) => {
    let { directoryPathUnique, directoryPath, zipName } = downloadData;

    // Change the zip name to be asci only characters
    zipName = zipName.replace(path.extname(zipName), '').replace(/[^\x00-\x7F]/g, '');
    console.log('Created zip: ' +zipName);

    res.set('Content-Type', 'application/zip');
    res.set('zip-file-name', zipName);

    // Zip all the content downloaded in the downloads folder
    const archive = archiver('zip', { zlib: { level: 9 } });

    // Delete the unique directory when the zip has been sent
    archive.on('end', () => {
        fs.rmdirSync(directoryPathUnique, { recursive: true });
    });

    archive.directory(`${directoryPath}/`, zipName);
    archive.pipe(res);
    archive.finalize();
}
