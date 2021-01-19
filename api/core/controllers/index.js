const fs = require("fs");
const path = require("path");
const archiver = require('archiver');
const dbManager = require('../db/dbManager');
const downloadsHandler = require('../utils/downloadsHandler');

// Logs in the user and saves the token in a cookie (for the website), use /user if you want to use the APIs manually instead
exports.login = async (req, res) => {
    try {
        // Login can only be made via authorization header
        if(res.locals.authViaCookie)    {
            return res.status(401)
                .json({ message: "Token not found, pass it in the Authorization header" });
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
                .json({ message: "Not authorized to create new users (not administrator)" });


        const newUser = await dbManager.newUser(req.body.token, req.body.administrator);
        if(newUser)
            res.status(200).json(newUser);
        else
            res.status(400).json({ message: "Either the token / administrator body parameter is missing or a user with that token already exists" });

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
            return res.status(400).send({ message: "Missing spotifyURL in request body" });

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
        const youtubeURL = req.body.youtubeURL;
        const audioOnly = req.body.audioOnly;

        if(!youtubeURL)
            return res.status(400).send({ message: "Missing youtubeURL in request body" });

        const downloadID = downloadsHandler.newDownload(null, youtubeURL, audioOnly);

        res.status(202).json({ downloadID: downloadID });
    } catch (e) {
        handleError(res, e);
    }
}

exports.getDownloadInfo = (req, res) => {
    try {
        const downloadInfo = downloadsHandler.getDownloadInfo(req.params.id);
        if(!downloadInfo)
            return res.status(400)
                .json({ message: "A download with the provided downloadID doesn't exist" });

        res.status(200).json(downloadInfo);
    } catch (e) {
        handleError(res, e);
    }
}

exports.getDownloadData = (req, res) => {
    try {
        const downloadInfo = downloadsHandler.getDownloadInfo(req.params.id);

        if(!downloadInfo)
            return res.status(400)
                .json({ message: "A download with the provided downloadID doesn't exist" });

        if(downloadInfo.errored)
            return res.status(404).json({ message: "The download has failed so it will not be available" });

        if(!downloadInfo.completed)
            return res.status(404).json({ message: "The download is still being processed so it's not available yet" });

        sendZip(downloadInfo, res);
    } catch (e) {
        handleError(res, e);
    }
}

const handleError = (res, e) => {
    console.log(e);
    res.status(500)
        .json({ message: `Something bad happened ¯\_(ツ)_/¯` });
}

const sendZip = (downloadInfo, res) => {
    let { directoryPathUnique, directoryPath, zipName } = downloadInfo;

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

    archive.on('error', err => {
        console.log(err.message);
        fs.rmdirSync(directoryPathUnique, { recursive: true });
    });

    archive.directory(`${directoryPath}/`, zipName);
    archive.pipe(res);
    archive.finalize();
}
