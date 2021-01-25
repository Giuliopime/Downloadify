const controller = require('../controllers');

module.exports = (app) => {
    app.route('/spotify')
        .post(controller.spotify);

    app.route('/spotify/:url')
        .post(controller.spotifyWithParam);

    app.route('/youtube')
        .post(controller.youtube);

    app.route('/youtube/:url')
        .post(controller.youtubeWithParam);

    app.route('/download-info/:id')
        .get(controller.getDownloadInfo);

    app.route('/download-data/:id')
        .get(controller.getDownloadData);
}