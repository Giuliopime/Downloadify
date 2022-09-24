import express, { Request, Response } from "express"
import ytdl from "ytdl-core"
import axios, { AxiosResponse } from "axios"
import UserModel from "../db/models/user.model"
import { OdesliResponse } from "../typing-stubs"

const router = express.Router()

type DownloadQuery = {
    url: string
}

// eslint-disable-next-line @typescript-eslint/no-misused-promises
router.get("/download-info",  async (req: Request<unknown, unknown, unknown, DownloadQuery>, res: Response) => {
    let url: string = decodeURIComponent(req.query.url)

    if (url.includes("spotify")) {
        const response: AxiosResponse<OdesliResponse> = await axios.get("https://api.song.link/v1-alpha.1/links?url=" + url + "&userCountry=IT")
        const youtubeUrl = response.data.linksByPlatform.youtube.url
        url = youtubeUrl.substring(1, youtubeUrl.length-1)
    }

    const vId = url.split('v=')[1]

    ytdl.getInfo(url, { lang: 'it' }).then(info => {
        return res.json({
            url: "https://youtube.com/embed/" + vId,
            info
        }).send()
    }).catch(e => {
        console.error("Error while retrieving video data with ytdl-core:", e)
        return res.status(500).send("Internal server error while getting video info")
    })
})


router.get("/download-started",  (req: Request, res: Response) => {
    UserModel.updateOne({"username": req.session.username}, { $inc : { "downloads": 1 } })
        .then(_ => res.status(200).send())
        .catch(e => {
            console.error("Could not update download count on user", e)
            res.status(500).send("Interval server error while registering the download")
        })
})

export { router as downloadRouter }
