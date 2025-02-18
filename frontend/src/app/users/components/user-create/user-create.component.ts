import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { UserService } from '../../services/user.service';
import { mergeMap, of, tap } from 'rxjs';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BlockUI } from 'ng-block-ui';
import { Router } from '@angular/router';
import { UserFormComponent } from '../user-form/user-form.component';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
    ReactiveFormsModule,
    UserFormComponent
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.scss'
})
export class UserCreateComponent {
  form: FormGroup

  constructor(
    private store: Store,
    private alerts: AlertService,
    private userService: UserService,
    private utilityService: UtilityService,
    private _formBuilder: FormBuilder,
    private router: Router,
  ) {
    this.form = this._formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      firstName: [''],
      lastName: [''],
      password: ['', Validators.required],
      confirm: ['', Validators.required],
    })
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
      this.userService.createUser(this.form.value),
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
          text: 'User created successfully',
        }))
      })
    ).subscribe();
  }

}
