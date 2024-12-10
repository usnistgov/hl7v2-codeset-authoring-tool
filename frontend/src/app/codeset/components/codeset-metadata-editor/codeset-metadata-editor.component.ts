import { Component } from '@angular/core';
import { DamAbstractEditorComponent, DamfEditorInitializer, ISaveResult, IStateCurrent, MessageType, UtilityService } from '@usnistgov/ngx-dam-framework';
import { ICodeset, ICodesetMetadata } from '../../models/codeset';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { CodesetService } from '../../services/codeset.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { CodesetState } from '../codeset-widget/codeset-widget.component';

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
  ],
  templateUrl: './codeset-metadata-editor.component.html',
  styleUrl: './codeset-metadata-editor.component.scss'
})
export class CodesetMetadataEditorComponent extends DamAbstractEditorComponent<ICodesetMetadata> {
  initialLabel?: string;
  form!: FormGroup;

  constructor(private codesetService: CodesetService,
    private utilityService: UtilityService,) {
    super({
      id: EDITOR_ID,
      title: 'Metadata',
    });

    this.form = new FormGroup({
      name: new FormControl(),
      description: new FormControl(),
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

  override onEditorDataUpdate(data: IStateCurrent<ICodesetMetadata, never>): void {
    if (data.initial) {
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
        CodesetState.setValue(this.store, res.data as ICodeset)
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
}
