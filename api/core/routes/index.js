const userRoutes = require('./usersRoutes');
const downloadRoutes = require('./downloadRoutes');
const dbManager = require('../db/dbManager');

module.exports = (app) => {
    // Default API auth
    app.use(async (req, res, next) => {
        let token = req.headers.authorization;

        let usedCookies = false;

        if(!token) {
            token = req.cookies ? req.cookies.token : null;
            usedCookies = true;
        }

        if(!token)
            return res.status(401)
                .send("Token not found, pass it either in the Authorization header or via the 'token' cookie");


        const user = await dbManager.getUser(token);
        if(!user)
            return res.status(401)
                .send("Token not valid");

        res.locals.user = user;
        res.locals.authViaCookie = usedCookies;

        next();
    })

    userRoutes(app);

    downloadRoutes(app);

    // Invalid routes
    app.use((req, res) =>
        res.status(404).send('"' + req.originalUrl + '" is not a valid route (not found)')
    );

    return app;
};
