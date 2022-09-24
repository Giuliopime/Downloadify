import express, { Request, Response } from "express"
import UserModel, {UserDocument} from "../db/models/user.model"

const router = express.Router()

router.get("/users/", (req: Request, res: Response) => {
    UserModel.find()
        .then(users => {
            res.json(users).send()
        })
        .catch(e => {
            console.error("Could not retrieve list of users", e)
            res.status(500).send("Internal server error while retrieving the list of users")
        })
})

router.get("/users/:username", (req: Request<{ username: string }>, res: Response) => {
    UserModel.findOne({"username": req.params.username})
        .then(user => {
            if (user === null)
                res.json(user).send()
            else
                res.status(404).send("User not found")
        })
        .catch(e => {
            console.error("Could not retrieve user", e)
            res.status(500).send("Internal server error while retrieving the user")
        })
})

router.post("/users", (req: Request, res: Response) => {
    const { username, password, admin } = req.body

    if (
        (username === undefined || typeof username !== "string")
        || (password === undefined || typeof password !== "string")
        || (admin === undefined || typeof admin !== "boolean")) {
            return res.status(400).send("Missing user data: username (string), password (string) and admin (boolean) are required")
    }

    new UserModel({
        username,
        password,
        admin
    }).save()
        .then(_ => res.status(200).send())
        .catch(e => {
            console.error("Could not create user", e)
            res.status(500).send("Internal server error while creating the user")
        })
})

router.put("/users/:username", (req: Request<{ username: string }, {}, UserDocument>, res: Response) => {
    const user: UserDocument = req.body

    if (user === undefined)
        return res.status(400).send("User data provided is not valid")

    user.save()
        .then(_ => res.status(200).send())
        .catch(e => {
            console.error("Failed updating user", e)
            res.status(500).send("Internal error while trying to update user")
        })

})

router.delete("/users/:username", (req: Request<{ username: string }>, res: Response) => {
    UserModel.deleteOne({ "username": req.params.username })
        .then(_ => res.status(200).send())
        .catch(e => {
            console.error(e)
            res.status(404).send("Username not found")
        })
})

export { router as adminRouter }
