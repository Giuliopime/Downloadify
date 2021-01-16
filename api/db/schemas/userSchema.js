const mongoose = require("mongoose");

const userSchema = mongoose.Schema({
    username: String,
    token: {
        type: String,
        required: true,
        unique: true,
        dropDups: true
    },
    administrator: {
        type: Boolean,
        required: true,
        dropDups: true
    }
});

module.exports = mongoose.model("userSchema", userSchema);
