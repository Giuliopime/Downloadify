import * as dotenv from "dotenv"
import express from "express"
import cors from "cors"
import routes from "./routes"
import connect from "./db";
import session from "express-session";
import {nanoid} from "nanoid";
import morgan from "morgan"

// Check for missing environment variables from .env file
dotenv.config()

const neededEnvVariables: string[] = [
    "PORT",
    "MONGODB_URI",
    "COOKIE_SECRET"
]

const missing: string|undefined = neededEnvVariables.find(e => process.env[e] === undefined)
if (missing !== undefined) {
  console.error(`Invalid .env configuration, missing ${missing}`)
  process.exit(1)
}


const PORT: number = parseInt(process.env.PORT!, 10)

const app = express()
// Logs all requests
app.use(morgan('combined'))
// This api is on a api. subdomain
app.use(cors())

app.use(express.json())

app.use(session({
  genid: function(req) {
    return nanoid() // Alternative to uuid
  },
  secret: process.env.COOKIE_SECRET!,
  resave: false,
  saveUninitialized: false,
  unset: "destroy",
  cookie: {
    secure: true
  }
}))

app.listen(PORT, () => {
  console.info(`Downloadify API listening on port ${PORT}`)

  // Connect to MongoDb
  void connect()
  
  routes(app)
})
