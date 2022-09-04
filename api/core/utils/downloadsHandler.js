const fs = require('fs');
const path = require('path');
const { exec } = require("child_process");
const { v4: uuidv4 } = require('uuid');
const spotifyUri = require('spotify-uri');

const downloads = [];

module.exports = {
    newDownload(spotifyURL, youtubeURL, audioOnly) {
        const downloadID = uuidv4();
        const downloadInfo = {
            downloadID: downloadID,
            spotifyURL: spotifyURL,
            youtubeURL: youtubeURL,
            audioOnly: audioOnly,
            state: 0,
            errored: false,
            completed: false,
        }
        downloads.push(downloadInfo);
        setTimeout(() => startDownload(downloadID), 0);

        return downloadID;
    },
    getDownloadInfo(downloadID) {
        return downloads[downloads.findIndex(data => data.downloadID === downloadID)];
    },
}


const startDownload = (downloadID) => {
    const downloadInfo = downloads[downloads.findIndex(data => data.downloadID === downloadID)];

    let  mainDirectoryName, URL, ytPlaylist = false;

    if(downloadInfo.spotifyURL) {
        mainDirectoryName = 'spotify-downloads';

        URL = downloadInfo.spotifyURL;
        try { URL = spotifyUri.formatOpenURL(downloadInfo.spotifyURL); } catch {}

    } else {
        mainDirectoryName = 'youtube-downloads';
        URL = downloadInfo.youtubeURL;
        if(URL.includes("playlist?"))
            ytPlaylist = true;
    }

    const { directoryPathUnique, directoryPath } = setupDirectoriesForDownload(mainDirectoryName, downloadID);

    /*
       youtube-dl command
       -o is the output
       --no-playlist makes it so if a yt link includes a playlist (other than a video) it downloads only the video and not the full playlist
       -f bestaudio[ext=m4a] downloads only the audio, in m4a format
    */

    const shellCommand = downloadInfo.spotifyURL ?
        `spotifydl ${URL} -o ${directoryPath}` :
        `youtube-dl -o "${directoryPath + (ytPlaylist ? '/%(playlist_title)s' : '') + '/%(title)s.%(ext)s'}" --no-playlist -f ${downloadInfo.audioOnly ? 'bestaudio --extract-audio --audio-format mp3' : 'bestvideo[ext=mp4]+bestaudio[ext=m4a]/mp4'} ${URL}`;

    downloadInfo.state = 1;

    exec(shellCommand, (error) => {
        if (error) {
            downloadInfo.errored = true;
            fs.rmdirSync(directoryPathUnique, { recursive: true });
            console.log(error)
            return;
        }
Ï
        downloadInfo.completed = true;
        downloadInfo.state = 2;

        downloadInfo.directoryPathUnique = directoryPathUnique;
        downloadInfo.directoryPath = directoryPath;
        downloadInfo.fileName = fs.readdirSync(directoryPath)[0];
    });
}

const setupDirectoriesForDownload = (mainDirectoryName, downloadID) => {
    // Create spotify-downloads & youtube-downloads folder if it doesn't yet exist
    if (!fs.existsSync(path.join(__dirname, '..', '..', mainDirectoryName)))
        fs.mkdirSync(path.join(__dirname, '..', '..') + '/' + mainDirectoryName)

    // Create unique directory to put files in
    const directoryPathUnique = path.join(__dirname, '..', '..', mainDirectoryName) + '/' + downloadID;
    fs.mkdirSync(directoryPathUnique);

    // Create directory to put downloads in
    const directoryPath = directoryPathUnique + '/downloads';
    fs.mkdirSync(directoryPath)

    return {
        directoryPathUnique: directoryPathUnique,
        directoryPath: directoryPath
    }
}

