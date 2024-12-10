import { Component } from '@angular/core';
import { DamAlertsContainerComponent } from '@usnistgov/ngx-dam-framework';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    DamAlertsContainerComponent
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {

}
