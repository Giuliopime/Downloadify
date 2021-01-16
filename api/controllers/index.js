const dbManager = require('../db/dbManager');

exports.login = async (req, res) => {
    const token = req.headers.authorization;
    const user = await dbManager.getUser(token);

    if(!user) {
        res.status(401)
            .send("Token not valid");
    } else {
        res.cookie('token', user.token, { httpOnly: true });
        res.status(200)
            .json({username: user.username})
    }
};

exports.getUser = async (req, res) => {
    const token = req.cookies ? req.cookies.token : null;

    if(!token) {
        res.status(401)
            .send("Token not found");
    } else {
        const user = await dbManager.getUser(token);
        if(!user) {
            res.status(401)
                .send("Token not valid");
        } else {
            res.status(200)
                .json({username: user.username})
        }
    }
}

exports.newUser = async (req, res) => {
    if(await dbManager.newUser(req.body.token, req.body.admin === "true"))
        res.status(200).send("User created successfully");
    else
        res.status(500).send("The server encountered an error while creating the user in the database");
}
