import express, { Request, Response } from "express"

const router = express.Router()

router.get("/ping", (req: Request, res: Response) => {

})

export { router as adminRouter }
