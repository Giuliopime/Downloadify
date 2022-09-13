import { adminRouter } from "./admin.route"
import { authRouter } from "./auth.route"
import { downloadRouter } from "./download.route"

import {Express, Request, Response} from "express";
import User, {UserDocument} from "../db/models/user.model";


export default function (app: Express): void {
    // Login page
    app.post("/login", (req: Request, res: Response) => {
        const loginData = req.body

        if (loginData.username === undefined)
            return res.status(400).send()

        // I hate this .then
        User.findOne({"username": loginData.username})
            .then(async u => {
                if (u === null)
                    return res.status(403).send()

                if (loginData.password === undefined || !await u.comparePassword(loginData.password))
                    return res.status(403).send()

                req.session.username = u.username
                req.session.admin = u.admin

                return res.status(200).send()
            }).catch(_ => {
                return res.status(403).send()
            }
        )
    })

    app.use(authRouter)
    app.use(adminRouter)
    app.use(downloadRouter)

    // Invalid routes
    app.use((req, res) =>
        res.status(404).send('"' + req.originalUrl + '" is not a valid route (not found)')
    );
}
