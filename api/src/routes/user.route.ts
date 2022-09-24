import express, { Request, Response } from "express"
import UserModel from "../db/models/user.model"

const router = express.Router()

router.get("/logout", (req: Request, res: Response) => {
    req.session.destroy(_ => {
        res.status(200).send()
    })
})

router.get("/@me", (req: Request, res: Response) => {
    UserModel.findOne({"username": req.session.username})
        .then(user => res.json(user).send())
        .catch(e => {
            console.error("Could not retrieve user", e)
            res.status(500).send("Internal error while retrieving your profile")
        })
})

export { router as userRouter }
