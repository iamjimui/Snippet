import { ROLE } from "./role";

export class User {

    private static instance: User;

    email = "";
    name = "";

    photoUrl = "";
    username = "";
    token = "";
    role = ROLE.ROLE_USER;
    id = 0;

    private constructor() { }

    public static getInstance(): User {
        if (!User.instance)
            User.instance = new User();

        return User.instance;
    }
}