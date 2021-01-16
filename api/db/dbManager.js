const mongoose = require("mongoose");
const userSchema = require("./schemas/userSchema");
const { DBNAME } = require("../config");

const dbOptions = {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    autoIndex: false,
    poolSize: 5,
    connectTimeoutMS: 10000,
    family: 4,
};

mongoose.connect("mongodb://localhost:27017/"+DBNAME, dbOptions);
mongoose.set("useFindAndModify", false);

mongoose.connection.on("connected", () => console.log("Connection to MongoDB server established"));

mongoose.connection.on("err", e => console.log("MongoDB error: \n" + e.stack));

mongoose.connection.on("disconnected", e => console.log("Disconnected from MongoDB server:\n" + e.stack));

module.exports = {
    async getUser(token) {
        return userSchema.findOne({token: token});
    },
    async newUser(token, admin) {
        const user = {
            token: token,
            administrator: admin
        }
        const userSaved = await new userSchema(user).save().catch(console.error)
        return userSaved ? true : false;
    },
    modifyUsername(token, username) {
        userSchema.findOneAndUpdate({ token: token}, { $set: { 'username': username}})
    },
    async disconnect() {
        await mongoose.connection.close();
    }
};
