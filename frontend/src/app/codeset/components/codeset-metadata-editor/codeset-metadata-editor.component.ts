import { Component } from '@angular/core';
import { ConfirmDialogComponent, DamAbstractEditorComponent, DamfEditorInitializer, ISaveResult, IStateCurrent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { ICodeset, ICodesetMetadata, ICodesetVersion } from '../../models/codeset';
import { catchError, map, Observable, of, take, tap } from 'rxjs';
import { CodesetService } from '../../services/codeset.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { CodesetState } from '../codeset-widget/codeset-widget.component';
import { DropdownModule } from 'primeng/dropdown';
import { loadCodeset } from '../../store/codeset.actions';
import { InputSwitchModule } from 'primeng/inputswitch';
import { MatDialog } from '@angular/material/dialog';
export const EDITOR_ID = 'TEXT_SECTION_EDITOR';

export const CODESET_METADATA_EDITOR_INITIALIZER: DamfEditorInitializer<ICodesetMetadata> = (params, injector) => {
  const codesetService = injector.get(CodesetService);
  return codesetService.getCodesetMetadata(params['codesetId']).pipe(
    map((value: ICodesetMetadata) => {
      return {
        initial: value as ICodesetMetadata,
        context: {
          elementId: value.id,
        }
      }
    })
  );
}

@Component({
  selector: 'app-codeset-metadata-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FaIconComponent,
    DropdownModule,
    InputSwitchModule,

  ],
  templateUrl: './codeset-metadata-editor.component.html',
  styleUrl: './codeset-metadata-editor.component.scss'
})
export class CodesetMetadataEditorComponent extends DamAbstractEditorComponent<ICodesetMetadata> {
  initialLabel?: string;
  form!: FormGroup;
  committedVersions: ICodesetVersion[] = [];
  constructor(private codesetService: CodesetService, private dialog: MatDialog,
    private utilityService: UtilityService,) {
    super({
      id: EDITOR_ID,
      title: 'Metadata',
    });

    this.form = new FormGroup({
      name: new FormControl(),
      description: new FormControl(),
      latestVersion: new FormControl(),
      disableKeyProtection: new FormControl(),
    });

    this.form.valueChanges.pipe(
      tap(() => {
        const previous = this.state.current();
        if (previous) {
          this.state.change({
            timestamp: new Date(),
            validation: {
              validated: true,
              valid: this.form.valid,
            },
            value: {
              ...previous.value,
              ...this.form.getRawValue()
            },
          });
        }
      })
    ).subscribe();
  }

  override onEditorDataUpdate(data: IStateCurrent<ICodeset, never>): void {
    if (data.initial) {
      this.committedVersions = data.value.versions.filter(v => v.dateCommitted !== null);

      this.form.patchValue(data.value, { emitEvent: false });
      this.initialLabel = data.value.name;
    }
  }
  override AfterEditorNgViewInit(): void {
  }
  override onEditorNgInit(): void {
  }
  override onEditorNgDestoy(): void {
  }
  override save(current: IStateCurrent<ICodesetMetadata, never>): Observable<ISaveResult<ICodeset>> {

    return this.codesetService.updateCodeset(current.value, current.value.id).pipe(
      map((res) => {
        console.log(res.data)
        if (res.data) {
          // CodesetState.setValue(this.store, res.data as ICodeset)
          this.store.dispatch(loadCodeset({ codesetId: res.data.id, redirect: false }));
        }


        return {
          success: true,
          message: res,
          data: res.data
        }
      }),
      catchError(err => {
        return of({
          success: false,
          message: err,
        })
      })
    )

  }

  onToggle(event: any) {
    if (event.checked) {
      console.log(event)
      const confirmDialog$ = this.dialog.open(ConfirmDialogComponent, {
        data: {
          action: 'Disable Key Protection',
          question: `By enabling API access without an API key, anybody with this code sets' link will be able to read its content. If you want to protect API access with an API key, create an API key by clicking on your username in the top right corner and selecting the "API Key" menu option. Are you sure you want to enable API access without an API key?`,
        },
      }).afterClosed();
      confirmDialog$.pipe(
        take(1),
        map((confirmed: boolean) => {
          if (confirmed) {
            this.form.patchValue({ disableKeyProtection: true });
          } else {
            this.form.patchValue({ disableKeyProtection: false });
          }
          return of();
        })
      ).subscribe();
    }
  }
}
