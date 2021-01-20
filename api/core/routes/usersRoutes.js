const controller = require('../controllers');

module.exports = (app) => {
    app.route('/user')
        .get(controller.getUser);

    app.route('/new-user')
        .get(controller.newUser);
}