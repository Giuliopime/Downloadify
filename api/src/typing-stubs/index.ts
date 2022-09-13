import session from "express-session";

declare module "express-session" {
    // eslint-disable-next-line
    export interface SessionData {
        username: string;
        admin: boolean;
    }
}
