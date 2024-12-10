import { Component } from '@angular/core';
import { AlertService, DamAlertsContainerComponent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { Store } from '@ngrx/store';
import { ApiKeyService } from '../../services/api-key.service';
import { of, tap } from 'rxjs';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BlockUI } from 'ng-block-ui';
import { Router } from '@angular/router';
import { CheckboxModule } from 'primeng/checkbox';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { loadCodesets } from '../../../codeset/store/codeset.actions';
import { ICodesetDescriptor } from '../../../codeset/models/codeset';
import { CodesetBrowserDialogComponent } from '../../../codeset/components/codeset-browser-dialog/codeset-browser-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { ApiKeyViewerDialogComponent } from '../api-key-viewer-dialog/api-key-viewer-dialog.component';

@Component({
  selector: 'app-api-key-create',
  standalone: true,
  imports: [
    DamAlertsContainerComponent,
    ReactiveFormsModule,
    CheckboxModule,
    FaIconComponent
  ],
  templateUrl: './api-key-create.component.html',
  styleUrl: './api-key-create.component.scss'
})
export class ApiKeyCreateComponent {
  form: FormGroup;
  selectedCodesets: ICodesetDescriptor[] = [];

  constructor(
    private store: Store,
    private alerts: AlertService,
    private apiKeyService: ApiKeyService,
    private utilityService: UtilityService,
    private _formBuilder: FormBuilder,
    private router: Router,
    private dialog: MatDialog
  ) {
    this.form = this._formBuilder.group({
      name: ['', Validators.required],
      expires: [true],
      validityDays: [30, [Validators.required, Validators.min(1)]],
    });
    this.store.dispatch(loadCodesets());

  }
  expiresChange(value: boolean) {
    if (!value) {
      this.form.controls['validityDays'].setValue(0);
      this.form.controls['validityDays'].disable();
    } else {
      this.form.controls['validityDays'].setValue(30);
      this.form.controls['validityDays'].enable();
    }
  }

  addCodeSet() {

    this.dialog.open(CodesetBrowserDialogComponent, {
      data: {
        selectedCodesets: this.selectedCodesets
      },
      width: '90%'
    }).afterClosed().subscribe({
      next: (codesets: ICodesetDescriptor[]) => {
        if (codesets) {
          this.selectedCodesets = [...codesets]
        }
      },
    });
  }
  removeCodeset(i: number) {
    this.selectedCodesets.splice(i, 1);
  }
  submit() {
    const form = this.form.value;
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
      this.apiKeyService.createApiKey({ ...this.form.value, codesets: this.selectedCodesets }),
      {
        loader: {
          blockUI: true
        }
      }
    ).pipe(
      tap((message) => {
        this.dialog.open(ApiKeyViewerDialogComponent, {
          disableClose: true,
          data: {
            key: message.data,
          },
        }).afterClosed().pipe(
          tap(() => {
            this.router.navigate(["/api-keys/list"]);
          }),
        ).subscribe();

      }),
    ).subscribe();
  }
}
