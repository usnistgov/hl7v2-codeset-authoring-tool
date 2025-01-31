import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { SelectItem } from 'primeng/api';
import { CheckboxModule } from 'primeng/checkbox';
import { TableModule } from 'primeng/table';
import { catchError, map, Observable, throwError } from 'rxjs';
import { CodesetService } from '../../services/codeset.service';
import { DeltaChange, ICodeDelta, ICodesetVersion } from '../../models/codeset';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-commit-codeset-version-dialog',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogActions,
    CheckboxModule,
    CommonModule,
    TableModule,
    DropdownModule
  ],
  templateUrl: './commit-codeset-version-dialog.component.html',
  styleUrl: './commit-codeset-version-dialog.component.scss'
})
export class CommitCodesetVersionDialogComponent {


  metaDataForm: FormGroup;
  title: string;
  resource$!: Observable<ICodesetVersion>;
  versions: SelectItem[];
  compareTarget!: ICodesetVersion;
  loading = false;
  delta: ICodeDelta[] | null = null;
  error: any = null;
  codeSetId: string;
  versionId: string;

  constructor(
    public dialogRef: MatDialogRef<CommitCodesetVersionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private codesetService: CodesetService,
  ) {
    this.title = data.title || 'Code Set Version';
    this.codeSetId = data.codesetId;
    this.versionId = data.versionId;
    this.metaDataForm = new FormGroup({
      version: new FormControl(data.version ? data.version || '' : '', [Validators.required, this.versionValidator()]),
      comments: new FormControl(data.comments ? data.comments || '' : '', []),
      latest: new FormControl(true),
    });
    this.versions = data.versions.filter((v: any) => v.dateCommitted && v.id !== this.versionId).map((v: any) => ({
      label: v.version,
      value: v,
    }));
    const found = this.versions.find((v) => (v.value as ICodesetVersion).latestStable);
    if (found) {
      this.compareTarget = found.value;
      this.loadDelta(this.compareTarget.id);
    }
  }
  loadDelta(id: string) {
    this.loading = true;
    this.error = null;
    this.codesetService.getCodeSetDelta(this.codeSetId, this.versionId, id).pipe(
      map((delta) => {
        this.delta = delta.filter((row) => row.change !== DeltaChange.NONE);
        this.loading = false;
      }),
      catchError((error) => {
        this.loading = false;
        this.error = error.message;
        this.delta = null;
        return throwError(error);
      }),
    ).subscribe();
  }

  onTargetChange($event: { id: string; }) {
    this.loadDelta($event.id);
  }

  cancel() {
    this.dialogRef.close();
  }

  create() {
    this.dialogRef.close(this.metaDataForm.getRawValue());
  }

  ngOnInit() {
  }

  versionValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const forbidden = control.value ? !/^[a-zA-Z0-9-_.]+$/.test(control.value) : false;
      return forbidden ? { invalidVersion: { value: control.value } } : {};
    };
  }

}
