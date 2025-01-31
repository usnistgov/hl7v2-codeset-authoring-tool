import { Component } from '@angular/core';
import { DamAbstractEditorComponent, DamfEditorInitializer, ISaveResult, IStateCurrent, MessageType } from '@usnistgov/ngx-dam-framework';
import { ITextSection } from '../../models/entity';
import { map, Observable, of, tap } from 'rxjs';
import { EntityService } from '../../services/entity.service';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

export const EDITOR_ID = 'TEXT_SECTION_EDITOR';

export const SECTION_TEXT_EDITOR_INITIALIZER: DamfEditorInitializer<ITextSection> = (params, injector) => {
  const entityService = injector.get(EntityService);
  return entityService.getSection(params['textSectionId']).pipe(
    map((value) => {
      return {
        initial: value as ITextSection,
        context: {
          elementId: value.id,
        }
      }
    })
  );
}

@Component({
  selector: 'app-entity-text-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FaIconComponent,
  ],
  templateUrl: './entity-text-editor.component.html',
  styleUrl: './entity-text-editor.component.scss'
})
export class EntityTextEditorComponent extends DamAbstractEditorComponent<ITextSection> {
  initialLabel?: string;
  form!: FormGroup;

  constructor() {
    super({
      id: EDITOR_ID,
      title: 'Text',
    });

    this.form = new FormGroup({
      label: new FormControl(),
      value: new FormControl(),
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

  override onEditorDataUpdate(data: IStateCurrent<ITextSection, never>): void {
    if (data.initial) {
      this.form.patchValue(data.value, { emitEvent: false });
      this.initialLabel = data.value.label;
    }
  }
  override AfterEditorNgViewInit(): void {
  }
  override onEditorNgInit(): void {
  }
  override onEditorNgDestoy(): void {
  }
  override save(current: IStateCurrent<ITextSection, never>): Observable<ISaveResult<ITextSection>> {
    return of({
      success: false,
      message: {
        status: MessageType.FAILED,
        text: 'Save not implemented',
      }
    });
  }
}
