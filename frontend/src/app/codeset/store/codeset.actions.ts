import { createAction, props } from "@ngrx/store";
import { ICodeset, ICodesetDescriptor, ICodesetVersion } from "../models/codeset";
import { IMessage } from "@usnistgov/ngx-dam-framework";


export const loadCodeset = createAction(
    '[Codeset] Load codeset',
    props<{ codesetId: string, redirect: boolean }>()
);
export const loadCodesetSuccess = createAction('[Codeset] Load codeset Success', props<{ data: ICodeset, redirect: boolean }>());
export const loadCodesetFailure = createAction('[Codeset] Load codeset Failure', props<{ error: any }>());

export const loadCodesets = createAction(
    '[Codesets] Load codesets');
export const loadCodesetsSuccess = createAction('[Codesets] Load codesets Success', props<{ data: ICodesetDescriptor[] }>());
export const loadCodesetsFailure = createAction('[Codesets] Load codesets Failure', props<{ error: any }>());



export const deleteCodesetVersion = createAction(
    '[Codeset Version] Delete codeset version',
    props<{ codesetId: string, codesetVersionId: string, redirect: boolean }>()
);
export const deleteCodesetVersionSuccess = createAction('[Codeset Version] Delete codeset version Success', props<{ data: IMessage<ICodeset>, redirect: boolean }>());
export const deleteCodesetVersionFailure = createAction('[Codeset Version] Delete codeset version Failure', props<{ error: any }>());