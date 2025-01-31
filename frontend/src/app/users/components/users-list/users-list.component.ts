import { Component, inject } from '@angular/core';
import {
  AlertService,
  DataStateRepository,
  IListItemControl,
  ListWidgetComponent,
  MessageType,
  SortOrder,
  UtilityService,
} from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { map, Observable, of, skip, switchMap, tap, throwError } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ActivatedRoute,
  ActivatedRouteSnapshot,
  Router,
  RouterLink,
  RouterLinkActive,
} from '@angular/router';
import { UserService } from '../../services/user.service';
import { IUser } from '../../models/user';
import { TableModule } from 'primeng/table';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { ConfirmationService } from 'primeng/api';
import { loadUsers } from '../../store/user.actions';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

export const UsersListState = new DataStateRepository<IUser>({
  name: 'userList', // unique name for this state variable
  routeLoader: (params, injector, state) => {
    // Hint (Error) : to throw an error while loading the state return an error observable with the error IMessage
    // return throwError(() => ({
    //   status: MessageType.FAILED,
    //   text: 'Could not load entity list',
    // }));
    const user = injector.get(UserService);
    return user.getUsersList();
  },
});

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [
    CommonModule,
    ListWidgetComponent,
    RouterLink,
    RouterLinkActive,
    FormsModule,
    TableModule,
    FaIconComponent,
    ConfirmDialogModule
  ],
  providers: [ConfirmationService],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.scss',
})
export class UsersListComponent {
  usersList = UsersListState;
  users$: Observable<IUser[]>;


  constructor(
    private store: Store,
    private routeSnapshot: ActivatedRoute,
    private userService: UserService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private utilityService: UtilityService,
  ) {
    this.users$ = UsersListState.findAll(this.store)

  }

  ngOnInit(): void {
    this.utilityService.clearAlerts();
  }
  createUser() {
    this.router.navigate(['/users/create'])
  }
  deleteUser(key: IUser) {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete User "' + key.username + '"?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      accept: () => {
        this.utilityService.useLoaderWithErrorAlert(
          this.userService.deleteUser(key.id),
          {
            loader: {
              blockUI: true
            }
          }
        ).pipe(
          tap((message) => {
            this.store.dispatch(loadUsers());
          }),
        ).subscribe();

      },
      reject: () => {
      },
      key: "positionDialog"
    });
  }
}
