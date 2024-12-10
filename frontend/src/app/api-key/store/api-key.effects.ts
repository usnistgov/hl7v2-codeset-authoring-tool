import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { mergeMap, map, catchError, of, tap } from "rxjs";
import { loadCodesets, loadCodesetsSuccess, loadCodesetsFailure } from "../../codeset/store/codeset.actions";
import { ApiKeyService } from "../services/api-key.service";
import { Store } from "@ngrx/store";
import { loadApiKeysFailure, loadApiKeys, loadApiKeysSuccess } from "./api-key.actions";
import { ApiKeyListState } from "../components/api-key-list/api-key-list.component";


@Injectable()
export class ApiKeyEffects {
    loadApiKeys$ = createEffect(() => inject(Actions).pipe(
        ofType(loadApiKeys),
        mergeMap((action) => this.apiKeyService.getApiKeys()
            .pipe(
                map(apiKeys => loadApiKeysSuccess({ data: apiKeys })),
                catchError((error) => of(loadApiKeysFailure({ error })))
            ))
    )
    );

    loadApiKeysSuccess$ = createEffect(() => inject(Actions).pipe(
        ofType(loadApiKeysSuccess),
        tap((action) => {
            ApiKeyListState.setValue(this.store, action.data)
        })
    ), { dispatch: false }
    );


    constructor(
        private apiKeyService: ApiKeyService,
        private store: Store,
    ) { }
}