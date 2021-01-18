const fs = require('fs');
const path = require('path');
const { exec } = require("child_process");
const archiver = require('archiver');
const { v4: uuidv4 } = require('uuid');
const spotifyUri = require('spotify-uri');

const downloadIDS = [];

module.exports = {
    newDownload(spotifyURL, youtubeURL) {
        const downloadID = uuidv4();
        downloadIDS.push({ downloadID: downloadID, spotifyURL: spotifyURL, youtubeURL: youtubeURL, state: "Processing..."});
        setTimeout(() => startDownload(downloadID), 0);

        return downloadID;
    },
    getDownloadStatus(downloadID) {
        return downloadIDS[downloadIDS.findIndex(data => data.downloadID === downloadID)];
    },
}


const startDownload = (downloadID) => {
    const downloadData = downloadIDS[downloadIDS.findIndex(data => data.downloadID === downloadID)];

    if(downloadData.spotifyURL) {
        let URL = downloadData.spotifyURL;
        try { URL = spotifyUri.formatOpenURL(downloadData.spotifyURL); } catch {}

        const { directoryPathUnique, directoryPath } = setupDirectoriesForDownload('spotify-downloads', downloadID);

        downloadData.state = "Downloading..."
        exec(`spotifydl ${URL} -o ${directoryPath}`, (error) => {
            if (error) {
                downloadData.error = true;
                return;
            }

            downloadData.finished = true;
            downloadData.state = "Receiving zip...";

            downloadData.directoryPathUnique = directoryPathUnique;
            downloadData.directoryPath = directoryPath;
            downloadData.zipName = fs.readdirSync(directoryPath)[0];
        });
    } else {
        const URL = downloadData.youtubeURL;
        const { directoryPathUnique, directoryPath } = setupDirectoriesForDownload('youtube-downloads', downloadID);
    }
}

const setupDirectoriesForDownload = (mainDirectoryName, downloadID) => {
    // Create spotify-downloads & youtube-downloads folder if it doesn't yet exist
    if (!fs.existsSync(path.join(__dirname, '..', '..', mainDirectoryName)))
        fs.mkdirSync(path.join(__dirname, '..', '..') + '/' + mainDirectoryName)

    // Create unique directory to put files in
    const directoryPathUnique = path.join(__dirname, '..', mainDirectoryName) + '/' + downloadID;
    fs.mkdirSync(directoryPathUnique);

    // Create directory to put downloads in
    const directoryPath = directoryPathUnique + '/downloads';
    fs.mkdirSync(directoryPath)

    return {
        directoryPathUnique: directoryPathUnique,
        directoryPath: directoryPath
    }
}

