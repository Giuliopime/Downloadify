const controller = require('../controllers');

module.exports = (app) => {
    app.route('/spotify')
        .post(controller.spotify);

    app.route('/youtube')
        .post(controller.youtube);

    app.route('/download-status/:id')
        .get(controller.downloadStatus);

    app.route('/get-download-data/:id')
        .get(controller.getDownloadedData);
}