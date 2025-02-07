import { Component, computed, Signal, signal, WritableSignal } from '@angular/core';
import { DamAbstractEditorComponent, DamfEditorInitializer, DataStateValue, ISaveResult, IStateCurrent, MessageHandlerMode, MessageType, selectRouteParams, UtilityService } from '@usnistgov/ngx-dam-framework';
import { catchError, combineLatest, flatMap, map, mergeMap, Observable, of, take, tap } from 'rxjs';
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
import { CodesetState, CodesetVersionsState } from '../codeset-widget/codeset-widget.component';

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
  codesetVersions$: Observable<ICodesetVersion[]>;
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
        const host = window.location.protocol + '//' + window.location.host;
        this.codeSetURL = host + '/api/v1/access/codesets/' + params['codesetId']
      })
    ).subscribe();
    this.codesetVersions$ = CodesetVersionsState.findAll(this.store)



  }

  override onEditorDataUpdate(data: IStateCurrent<ICodesetVersion, never>): void {
    this.codesetVersion.set({ ...data.value });
    if (data.source === "INITIAL") {
      this.versionURL = `${this.codeSetURL}?version=${this.codesetVersion().version}`;
    }
  }

  public update(event: { codes: ICodesetVersionCode[], valid: boolean | null }): void {
    this.state.change({
      timestamp: new Date(),
      validation: {
        validated: true,
        valid: event.valid as boolean
      },
      value: {
        ...this.codesetVersion(),
        codes: event.codes
      },
    });
  }

  public commitDialog() {
    combineLatest([this.store.select(selectRouteParams), this.codesetVersions$]).pipe(
      take(1),
      mergeMap((data: any) => {
        return this.dialog.open(CommitCodesetVersionDialogComponent, {
          height: '75vh',
          width: '90vw',
          position: {
            top: '15vh',
            left: '10vw'
          },
          data: {
            title: 'Commit Code Set Version',
            comments: '',
            version: this.codesetVersion().version,
            versionId: this.codesetVersion().id,
            codesetId: data[0]['codesetId'],
            versions: data[1]

          },
        })
          .afterClosed()
          .pipe(
            mergeMap((res) => {
              if (res) {
                return this.commit(res);
              }
              return of();
            })
          )
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
          fromHttpResponse: true,
          mode: MessageHandlerMode.MESSAGE_RESULT_AND_ERROR
        }

      }
    ).pipe(
      map(res => {
        CodesetState.getOneValue(this.store).pipe(
          take(1),
          map((codeset) => {
            this.store.dispatch(loadCodeset({ codesetId: codeset.id, redirect: true }));

          })
        ).subscribe()



      })
    )
  }

  override AfterEditorNgViewInit(): void {
  }
  override onEditorNgInit(): void {

  }
  override onEditorNgDestoy(): void {
  }
  override save(current: IStateCurrent<ICodesetVersion, never>): Observable<ISaveResult<ICodesetVersion>> {

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
        if (codes) {
          this.update({ codes, valid: true });
        }
      },
    });
  }

  exportCSV($event: ICodesetVersion) {
    // this.codesetService.exportCSV($event.id);
    this.store.select(selectRouteParams).pipe(
      take(1),
      map((params: Record<string, string>) => {
        this.codesetService.exportCSV(params['codesetId'], params['versionId'])
      })
    ).subscribe()
  }
}
