import { Component, computed, Signal, signal, WritableSignal } from '@angular/core';
import { DamAbstractEditorComponent, DamfEditorInitializer, DataStateValue, ISaveResult, IStateCurrent, MessageHandlerMode, MessageType, selectRouteParams, UtilityService } from '@usnistgov/ngx-dam-framework';
import { catchError, flatMap, map, mergeMap, Observable, of, take, tap } from 'rxjs';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { MatDialog } from "@angular/material/dialog";
import { AddFieldDialogComponent } from '../add-field-dialog/add-field-dialog.component';
import { CodesetService } from '../../services/codeset.service';
import { ICodesetVersion, ICodesetVersionCode, ICodesetVersionCommit } from '../../models/codeset';
import { CommitCodesetVersionDialogComponent } from '../commit-codeset-version-dialog/commit-codeset-version-dialog.component';
import { CodesetTableComponent } from '../codeset-table/codeset-table.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ImportCodesDialogComponent } from '../import-codes-dialog/import-codes-dialog.component';
import { InputCopyComponent } from '../input-copy/input-copy.component';
import { loadCodeset } from '../../store/codeset.actions';
import { CodesetState } from '../codeset-widget/codeset-widget.component';

export const EDITOR_ID = 'FORM_SECTION_EDITOR';

export const CODESET_VERSION_EDITOR_INITIALIZER: DamfEditorInitializer<ICodesetVersion> = (params, injector) => {
  const codesetService = injector.get(CodesetService);
  return codesetService.getCodesetVersion(params['codesetdId'], params['versionId']).pipe(
    map((value) => {
      return {
        initial: value as ICodesetVersion,
        context: {
          elementId: value.id,
        }
      }
    })
  );
}


@Component({
  selector: 'app-codeset-version-editor',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FaIconComponent,
    CodesetTableComponent,
    InputCopyComponent,
  ],
  templateUrl: './codeset-version-editor.component.html',
  styleUrl: './codeset-version-editor.component.scss'
})
export class CodesetVersionEditorComponent extends DamAbstractEditorComponent<ICodesetVersion> {
  form: WritableSignal<FormGroup | undefined>;
  codesetVersion: WritableSignal<ICodesetVersion> = signal({ id: '', version: '', codes: [], codeSystems: [] });
  selectedColumns: any[] = [];
  cols: any[] = [];
  codesetId: string = '';
  versionURL: string = '';
  codeSetURL: string = '';

  constructor(private dialog: MatDialog, private codesetService: CodesetService, private route: ActivatedRoute, private utilityService: UtilityService,
    private router: Router,
  ) {
    super({
      id: EDITOR_ID,
      title: 'Codeset Version',
    });
    this.form = signal(undefined);
    this.cols = [];
    this.cols.push({ field: 'code', header: 'Value' });
    this.cols.push({ field: 'pattern', header: 'Pattern' });
    this.cols.push({ field: 'description', header: 'Description' });
    this.cols.push({ field: 'system', header: 'Code System' });
    this.cols.push({ field: 'usage', header: 'Usage' });
    this.cols.push({ field: 'comments', header: 'Comments' });
    this.selectedColumns = this.cols;
    this.store.select(selectRouteParams).pipe(
      take(1),
      map((params: Record<string, string>) => {
        console.log(params)
        const host = window.location.protocol + '//' + window.location.host;
        this.versionURL = host + '/codesets/' + params['codesetId'] + '/versions/' + params['versionId'];
        this.codeSetURL = host + '/codesets/' + params['codesetId']
      })
    ).subscribe();


  }

  override onEditorDataUpdate(data: IStateCurrent<ICodesetVersion, never>): void {
    this.codesetVersion.set({ ...data.value });

  }

  public update(codes: ICodesetVersionCode[]): void {
    this.state.change({
      timestamp: new Date(),
      validation: {
        validated: false,
      },
      value: {
        ...this.codesetVersion(),
        codes: codes
        // label: formContent['label'],
        // fields: Object.keys(fieldsMetadata)
        //   .map((key) => ({
        //     key,
        //     value: formContent[key],
        //     label: fieldsMetadata[key].label,
        //     disabled: fieldsMetadata[key].disabled,
        //     required: fieldsMetadata[key].required,
        //   }))
      },
    });
  }

  public commitDialog() {
    this.dialog.open(CommitCodesetVersionDialogComponent, {
      data: {
        title: 'Commit Code Set Version',
        comments: '',
        version: this.codesetVersion().version,
      },
    })
      .afterClosed()
      .pipe(
        mergeMap((res) => {
          if (res) {
            console.log(res);
            // resource.version = res.version;
            // resource.comments = res.comments;
            // resource.latest = res.latest;
            // this.saveAndUpdate(parent, resource);
            return this.commit(res);
          }
          return of();
        })
      ).subscribe();
  }

  commit(codesetVersionCommit: ICodesetVersionCommit) {
    return this.utilityService.use(
      this.store.select(selectRouteParams).pipe(
        take(1),
        mergeMap((params: Record<string, string>) => {
          return this.codesetService.commitCodesetVersion({ ...codesetVersionCommit, codes: this.codesetVersion().codes }, params['codesetId'], params['versionId']);
        })
      ),
      {
        loader: {
          blockUI: true
        },
        alert: {
          mode: MessageHandlerMode.MESSAGE_RESULT_AND_ERROR
        }

      }
    ).pipe(
      map(res => {
        CodesetState.getOneValue(this.store).pipe(
          take(1),
          map((codeset) => {
            this.store.dispatch(loadCodeset({ codesetId: codeset.id }));

          })
        ).subscribe()



      })
    )
  }

  override AfterEditorNgViewInit(): void {
  }
  override onEditorNgInit(): void {
    this.codesetId = this.route.snapshot.params['codesetId'];
    console.log(this.codesetId, this.route.snapshot.paramMap)
  }
  override onEditorNgDestoy(): void {
  }
  override save(current: IStateCurrent<ICodesetVersion, never>): Observable<ISaveResult<ICodesetVersion>> {

    console.log("Saving :", current);
    return this.store.select(selectRouteParams).pipe(
      take(1),
      mergeMap((params: Record<string, string>) => {
        return this.codesetService.updateCodesetVersion(current.value, params['codesetId'], current.value.id).pipe(
          map((res) => {
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
      })
    )

  }

  importCSV() {
    this.dialog.open(ImportCodesDialogComponent, {
    }).afterClosed().subscribe({
      next: (codes: ICodesetVersionCode[]) => {
        console.log(codes);
        if (codes) {
          this.update(codes);
        }
      },
    });
  }

  exportCSV($event: ICodesetVersion) {
    console.log("--")
    // this.codesetService.exportCSV($event.id);
    this.store.select(selectRouteParams).pipe(
      take(1),
      map((params: Record<string, string>) => {
        this.codesetService.exportCSV(params['codesetId'], params['versionId'])
      })
    ).subscribe()
  }
}
