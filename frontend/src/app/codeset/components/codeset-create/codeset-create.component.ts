import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { CodesetService } from '../../services/codeset.service';
import { tap } from 'rxjs';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BlockUI } from 'ng-block-ui';
import { Router } from '@angular/router';

@Component({
  selector: 'app-entity-create',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
    ReactiveFormsModule
  ],
  templateUrl: './codeset-create.component.html',
  styleUrl: './codeset-create.component.scss'
})
export class CodesetCreateComponent {
  form: FormGroup

  constructor(
    private store: Store,
    private alerts: AlertService,
    private codesetService: CodesetService,
    private utilityService: UtilityService,
    private _formBuilder: FormBuilder,
    private router: Router,
  ) {
    this.form = this._formBuilder.group({
      name: ['', Validators.required],
      description: ['']
    })
  }

  submit() {
    this.utilityService.useLoaderWithErrorAlert(
      this.codesetService.createCodeset(this.form.value),
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
      tap((message) => {
        this.router.navigate(["/codesets", message.id, 'dashboard']);

      }),
    ).subscribe();

    // this.store.dispatch(
    //   this.alerts.getAlertAddAction({
    //     status: MessageType.WARNING,
    //     text: 'Feature not implemented',
    //   }, {
    //     tags: ['CREATE_ENTITY_ISSUES']
    //   })
    // );
  }
}
