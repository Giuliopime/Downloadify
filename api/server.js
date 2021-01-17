const express = require("express");
const bodyParser = require('body-parser');
const routes = require("./routes");
const cors = require('cors');
const cookieParser = require('cookie-parser');
const { APIPORT } = require('./config.js');

const app = express();
app.use(cookieParser());
app.use(cors({ origin: true, credentials: true, exposedHeaders: 'zip-file-name', }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// API routes
routes(app);

// Invalid routes
app.use((req, res) =>
    res.status(404).send('"' + req.originalUrl + '" is not a valid route (not found)')
)


app.listen(APIPORT, () =>
    console.log(`Downloadify API launched on port ${APIPORT} [http://localhost:${APIPORT}]`)
);
