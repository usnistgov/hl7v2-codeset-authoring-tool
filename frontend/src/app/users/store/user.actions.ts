import { createAction, props } from "@ngrx/store";
import { IUser } from "../models/user";


export const loadUsers = createAction(
    '[User] Load Users',);
export const loadUsersSuccess = createAction('[User] Load Users Success', props<{ data: IUser[] }>());
export const loadUsersFailure = createAction('[User] Load Users Failure', props<{ error: any }>());
