import mongoose from "mongoose"
import bcrypt from "bcrypt"

export type UserDocument = {
    username: string
    password: string
    admin: boolean
    downloads: number
    comparePassword: (candidatePassword: string) => Promise<boolean>
} & mongoose.Document

const UserSchema = new mongoose.Schema(
    {
        username: { type: String, required: true, index: { unique: true } },
        password: { type: String, required: true },
        admin: { type: Boolean, required: true, default: false },
        downloads: { type: Number, required: true, default: 0 }
    },
    { timestamps: true }
)

UserSchema.pre("save", async function(next) {
    const user = this as UserDocument

    // only hash the password if it has been modified (or is new)
    if (!user.isModified("password"))
        return next() // Honestly, I don't know why I made this api in typescript, but I kinda regret it

    // Random additional data
    const salt = await bcrypt.genSalt(10)

    // Replace the password with the hash
    user.password = bcrypt.hashSync(user.password, salt)

    return next()
})

// Used for logging in
UserSchema.methods.comparePassword = async function (
    candidatePassword: string
) {
    const user = this as UserDocument

    return await bcrypt.compare(candidatePassword, user.password).catch(_ => false)
}

const UserModel = mongoose.model<UserDocument>("user", UserSchema)

export default UserModel
