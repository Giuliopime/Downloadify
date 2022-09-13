import express, { Request, Response } from "express"

const router = express.Router()

router.get("/login", (req: Request, res: Response) => {

})

export { router as authRouter }
