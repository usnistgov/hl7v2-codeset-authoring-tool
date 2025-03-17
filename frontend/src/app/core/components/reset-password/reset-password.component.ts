import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DamAlertsContainerComponent, UtilityService } from '@usnistgov/ngx-dam-framework';
import { ApiKeyService } from '../../../api-key/services/api-key.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../api-key/services/auth.service';
import { tap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
    ReactiveFormsModule,
  ],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent {
  form: FormGroup;
  token!: string;
  constructor(
    private utilityService: UtilityService,
    private _formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.token = this.route.snapshot.queryParams['token'];
    this.form = this._formBuilder.group({
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  sendResetLink() {
    this.utilityService.useLoaderWithErrorAlert(
      this.authService.resetPassword(this.token, this.form.value.password),
      {
        message: {
          fromHttpResponse: true,
        },
        loader: {
          blockUI: true
        }
      }
    ).pipe(
      tap((message) => {
        console.log(message);
        this.router.navigate(["/login"]);

      }),
    ).subscribe();
  }
}
