const express = require("express");
const bodyParser = require('body-parser');
const routes = require("./routes");
const cors = require('cors');
const cookieParser = require('cookie-parser');
const dbManager = require('./db/dbManager');
const { APIPORT } = require('./config.js');

const app = express();
app.use(cookieParser());
app.use(cors({ origin: true, credentials: true, exposedHeaders: 'zip-file-name' }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

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
// API routes
routes(app);

// Invalid routes
app.use((req, res) =>
    res.status(404).send('"' + req.originalUrl + '" is not a valid route (not found)')
)


app.listen(APIPORT, () =>
    console.log(`Downloadify API launched on port ${APIPORT} [http://localhost:${APIPORT}]`)
);
