import { inject, Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { mergeMap, map, catchError, of, tap } from "rxjs";
import { Store } from "@ngrx/store";
import { loadUsersFailure, loadUsers, loadUsersSuccess } from "./user.actions";
import { UserService } from "../services/user.service";
import { UsersListState } from "../components/users-list/users-list.component";


@Injectable()
export class UserEffects {
    loadUsers$ = createEffect(() => inject(Actions).pipe(
        ofType(loadUsers),
        mergeMap((action) => this.userService.getUsersList()
            .pipe(
                map(users => loadUsersSuccess({ data: users })),
                catchError((error) => of(loadUsersFailure({ error })))
            ))
    )
    );

    loadUsersSuccess$ = createEffect(() => inject(Actions).pipe(
        ofType(loadUsersSuccess),
        tap((action) => {
            UsersListState.setValue(this.store, action.data)
        })
    ), { dispatch: false }
    );


    constructor(
        private userService: UserService,
        private store: Store,
    ) { }
}