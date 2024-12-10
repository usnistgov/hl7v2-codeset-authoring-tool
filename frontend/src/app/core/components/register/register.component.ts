import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    DamAlertsContainerComponent
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  constructor(
    private store: Store,
    private alerts: AlertService,
  ) { }

  submit() {
    this.store.dispatch(
      this.alerts.getAlertAddAction({
        status: MessageType.WARNING,
        text: 'Feature not implemented',
      }, {
        tags: ['REGISTER_ISSUES']
      })
    );
  }
}
