import { createAction, props } from "@ngrx/store";
import { IAPIKeyDisplay } from "../models/api-key";


export const loadApiKeys = createAction(
    '[Api Key] Load Api Keys',);
export const loadApiKeysSuccess = createAction('[Api Key] Load Api Keys Success', props<{ data: IAPIKeyDisplay[] }>());
export const loadApiKeysFailure = createAction('[Api Key] Load Api Keys Failure', props<{ error: any }>());
