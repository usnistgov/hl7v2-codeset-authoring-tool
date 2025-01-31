import { Component, computed, Signal, signal, WritableSignal } from '@angular/core';
import { DamAbstractEditorComponent, DamfEditorInitializer, ISaveResult, IStateCurrent, MessageType } from '@usnistgov/ngx-dam-framework';
import { IFormSection } from '../../models/entity';
import { EntityService } from '../../services/entity.service';
import { map, Observable, of, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { MatDialog } from "@angular/material/dialog";
import { AddFieldDialogComponent } from '../add-field-dialog/add-field-dialog.component';

export const EDITOR_ID = 'FORM_SECTION_EDITOR';

export const SECTION_FORM_EDITOR_INITIALIZER: DamfEditorInitializer<IFormSection> = (params, injector) => {
  const entityService = injector.get(EntityService);
  return entityService.getSection(params['formSectionId']).pipe(
    map((value) => {
      return {
        initial: value as IFormSection,
        context: {
          elementId: value.id,
        }
      }
    })
  );
}

export interface IFieldMetadata {
  label: string;
  required: boolean;
  disabled: boolean;
}

@Component({
  selector: 'app-entity-form-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FaIconComponent,
  ],
  templateUrl: './entity-form-editor.component.html',
  styleUrl: './entity-form-editor.component.scss'
})
export class EntityFormEditorComponent extends DamAbstractEditorComponent<IFormSection> {
  form: WritableSignal<FormGroup | undefined>;
  fieldsMetadata!: WritableSignal<Record<string, IFieldMetadata>>;
  fieldsList!: Signal<Array<IFieldMetadata & { key: string }>>;
  initialLabel: WritableSignal<string> = signal('');

  constructor(private dialog: MatDialog) {
    super({
      id: EDITOR_ID,
      title: 'Form',
    });
    this.form = signal(undefined);
    this.fieldsMetadata = signal({});
    this.fieldsList = computed(() => {
      const fieldsMetadata = this.fieldsMetadata();
      return Object.keys(fieldsMetadata).map((key) => ({
        key,
        label: fieldsMetadata[key].label,
        required: fieldsMetadata[key].required,
        disabled: fieldsMetadata[key].disabled,
      }));
    });
  }

  override onEditorDataUpdate(data: IStateCurrent<IFormSection, never>): void {
    if (data.initial) {
      // Initialize form and metadata
      const fields: Record<string, FormControl> = {
        label: new FormControl({ value: data.value.label, disabled: false }),
      };
      let metadata: Record<string, IFieldMetadata> = {};
      for (const field of data.value.fields) {
        fields[field.key] = new FormControl({
          value: field.value,
          disabled: false,
        }, [Validators.required]);
        metadata = {
          ...metadata,
          [field.key]: {
            label: field.label,
            required: true,
            disabled: false
          },
        };
      }
      this.fieldsMetadata.set(metadata);
      this.form.set(new FormGroup(fields));
      // Set the initial name for this link
      this.initialLabel.set(data.value.label);
      // Set form update listener
      this.form()?.valueChanges.pipe(
        tap(() => {
          const previous = this.state.current();
          const formContent = this.form()?.getRawValue();
          this.update(previous?.value, formContent)
        })
      ).subscribe();
    }
  }

  public addFieldDialog() {
    this.dialog.open(AddFieldDialogComponent, {})
      .afterClosed()
      .pipe(
        tap((field) => {
          if (field) {
            this.addField(field.key, field.label, field.required);
          }
        })
      ).subscribe();
  }

  public update(previous: IFormSection | undefined, formContent: Record<string, string>): void {
    if (previous) {
      const fieldsMetadata = this.fieldsMetadata();
      this.state.change({
        timestamp: new Date(),
        validation: {
          validated: true,
          valid: this.form()?.valid,
        },
        value: {
          ...previous,
          label: formContent['label'],
          fields: Object.keys(fieldsMetadata)
            .map((key) => ({
              key,
              value: formContent[key],
              label: fieldsMetadata[key].label,
              disabled: fieldsMetadata[key].disabled,
              required: fieldsMetadata[key].required,
            }))
        },
      });
    }
  }

  public removeField(key: string): void {
    if (this.fieldsMetadata()[key] && this.form()) {
      this.fieldsMetadata.update((value) => {
        const changed = {
          ...value,
        };
        delete changed[key];
        return changed;
      });
      this.update(this.current()?.value, this.form()?.getRawValue());
    }
  }

  public addField(key: string, label: string, required: boolean): void {
    if (!this.fieldsMetadata()[key] && this.form()) {
      this.fieldsMetadata.update((value) => ({
        ...value,
        [key]: {
          label,
          required,
          disabled: false
        }
      }));
      this.form()?.addControl(key, new FormControl({
        value: '',
        disabled: false,
      }, required ? [Validators.required] : []));
      this.update(this.current()?.value, this.form()?.getRawValue());
    }
  }

  override AfterEditorNgViewInit(): void {
  }
  override onEditorNgInit(): void {
  }
  override onEditorNgDestoy(): void {
  }
  override save(current: IStateCurrent<IFormSection, never>): Observable<ISaveResult<IFormSection>> {
    return of({
      success: false,
      message: {
        status: MessageType.FAILED,
        text: 'Save not implemented',
      }
    });
  }
}
