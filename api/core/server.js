const express = require("express");
const bodyParser = require('body-parser');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const router = express.Router();
const routes = require("./routes");
const { APIPORT } = require('../config.js');

const app = express();
app.use(cookieParser());
app.use(cors({ origin: true, credentials: true, exposedHeaders: 'zip-file-name' }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use('/api', routes(router));

app.listen(APIPORT, () =>
    console.log(`Downloadify API launched on port ${APIPORT} [http://localhost:${APIPORT}]`)
);
