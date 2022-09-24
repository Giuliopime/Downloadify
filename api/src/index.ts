import * as dotenv from "dotenv"
import express from "express"
import cors from "cors"
import routes from "./routes"
import connect from "./db"
import session from "express-session"
import {nanoid} from "nanoid"
import morgan from "morgan"
import connect_redis from "connect-redis"
import {createClient} from "redis"

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

// Connect to redis for storing sessions
const RedisStore = connect_redis(session)
const redisClient = createClient({
  url: process.env.REDIS_URL,
  legacyMode: true,
})
redisClient.connect().catch(console.error)
redisClient.on("error", console.error)


const PORT: number = parseInt(process.env.PORT!, 10)

const app = express()
// Logs all requests
app.use(morgan('combined'))
// This api is on a subdomain
app.use(cors())

app.use(express.json())

app.use(session({
  genid: function(_) {
    return nanoid() // Alternative to uuid
  },
  store: new RedisStore({ client: redisClient }),
  secret: process.env.COOKIE_SECRET!,
  resave: false,
  saveUninitialized: false,
  unset: "destroy",
  cookie: {
    secure: true // https
  }
}))

app.listen(PORT, () => {
  console.info(`Downloadify API listening on port ${PORT}`)

  // Connect to MongoDb
  void connect()

  routes(app)
})
