const controller = require('../controllers');

module.exports = (app) => {
    app.route('/spotify')
        .post(controller.spotify);

    app.route('/youtube')
        .post(controller.youtube);
}