import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-entity-create',
  standalone: true,
  imports: [
    DamAlertsContainerComponent
  ],
  templateUrl: './entity-create.component.html',
  styleUrl: './entity-create.component.scss'
})
export class EntityCreateComponent {
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
        tags: ['CREATE_ENTITY_ISSUES']
      })
    );
  }
}
