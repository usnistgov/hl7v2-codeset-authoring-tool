export interface IUserData {
    firstName: string;
    lastName: string;
    email: string;
    username: string;
}

export interface IUser extends IUserData {
    id: string;

}