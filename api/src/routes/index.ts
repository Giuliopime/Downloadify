import { adminRouter } from "./admin.route"
import { userRouter } from "./user.route"
import { downloadRouter } from "./download.route"

import {Express, Request, Response} from "express"
import UserModel from "../db/models/user.model"


export default function (app: Express): void {
    // Login handling
    app.post("/login", (req: Request, res: Response) => {
        const loginData = req.body

        if (loginData.username === undefined)
            return res.status(400).send("Bad request, username is missing")

        // I hate this .then
        UserModel.findOne({"username": loginData.username})
            .then(async u => {
                if (u === null)
                    return res.status(403).send() // Don't put a message here for security reasons

                if (loginData.password === undefined || !await u.comparePassword(loginData.password))
                    return res.status(403).send() // Don't put a message here for security reasons

                req.session.username = u.username
                req.session.admin = u.admin

                return res.status(200).send()
            }).catch(_ => {
                return res.status(500).send("Internal server error while trying to login")
            }
        )
    })

    app.use(userRouter)
    app.use(adminRouter)
    app.use(downloadRouter)

    // Invalid routes
    app.use((req, res) =>
        res.status(404).send('"' + req.originalUrl + '" is not a valid route (not found)')
    )
}
