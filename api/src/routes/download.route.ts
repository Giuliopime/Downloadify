import express, { Request, Response } from "express"

const router = express.Router()

router.post("/start-download", (req: Request, res: Response) => {

})

router.get("/download-info", (req: Request, res: Response) => {

})

router.get("/download", (req: Request, res: Response) => {

})

export { router as downloadRouter }
