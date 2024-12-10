import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { CheckboxModule } from 'primeng/checkbox';

@Component({
  selector: 'app-commit-codeset-version-dialog',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    CheckboxModule,
    CommonModule
  ],
  templateUrl: './commit-codeset-version-dialog.component.html',
  styleUrl: './commit-codeset-version-dialog.component.scss'
})
export class CommitCodesetVersionDialogComponent {


  metaDataForm: FormGroup;
  title: string;

  constructor(
    public dialogRef: MatDialogRef<CommitCodesetVersionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.title = data.title || 'Code Set Version';
    this.metaDataForm = new FormGroup({
      version: new FormControl(data.version ? data.version || '' : '', [Validators.required, this.versionValidator()]),
      comments: new FormControl(data.comments ? data.comments || '' : '', []),
      latest: new FormControl(true),
    });
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
