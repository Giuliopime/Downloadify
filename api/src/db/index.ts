import * as mongoose from "mongoose"

if (process.env.MONGODB_URI === undefined) {
    console.error("Invalid .env configuration, missing MONGODB_URI")
    process.exit(1)
}

const URI: string = process.env.MONGODB_URI

export default async function connect(): Promise<void> {
    try {
        await mongoose.connect(URI)
    } catch (e) {
        console.error("Could not connect to database", e)
        process.exit(1)
    }
}
