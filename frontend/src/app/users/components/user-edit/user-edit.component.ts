import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { UserService } from '../../services/user.service';
import { mergeMap, of, tap } from 'rxjs';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BlockUI } from 'ng-block-ui';
import { ActivatedRoute, Router } from '@angular/router';
import { UserFormComponent } from '../user-form/user-form.component';

@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
    ReactiveFormsModule,
    UserFormComponent
  ],
  templateUrl: './user-edit.component.html',
  styleUrl: './user-edit.component.scss'
})
export class UserEditComponent {
  userId: string;
  form!: FormGroup;
  constructor(
    private store: Store,
    private alerts: AlertService,
    private userService: UserService,
    private utilityService: UtilityService,
    private _formBuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    // get params from the route id
    this.userId = this.activatedRoute.snapshot.params['id'];
    this.initilizeForm();

  }

  initilizeForm() {
    this.userService.getUser(this.userId).pipe(
      tap((user) => {
        this.form = this._formBuilder.group({
          username: [user.username, Validators.required],
          email: [user.email, Validators.required],
          firstName: [user.firstName],
          lastName: [user.lastName],
          password: [''],
          confirm: [''],
        })
      })
    ).subscribe();
  }
  submit(form: any) {
    if (form.password !== form.confirm) {
      this.store.dispatch(
        this.alerts.getAlertAddAction({
          status: MessageType.FAILED,
          text: "Password and Confirm Password don't match",
        }, {
          tags: ['CREATE_ENTITY_ISSUES']
        })
      );
      return;
    }
    this.utilityService.useLoaderWithErrorAlert(
      this.userService.editUser(this.userId, this.form.value),
      {
        message: {
          fromHttpResponse: true,
          tags: ['CREATE_ENTITY_ISSUES']
        },
        loader: {
          blockUI: true
        }
      }
    ).pipe(
      mergeMap((message) => {
        this.router.navigate(["/users/list"]);
        return this.utilityService.useEmitSuccessAlert(of({
          status: MessageType.SUCCESS,
          text: 'User updated successfully',
        }))
      })
    ).subscribe();
  }

}
