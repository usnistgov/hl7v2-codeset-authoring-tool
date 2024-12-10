import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";

@Component({
  selector: 'app-add-field-dialog',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
  ],
  templateUrl: './add-field-dialog.component.html',
  styleUrl: './add-field-dialog.component.scss'
})
export class AddFieldDialogComponent {

  form = new FormGroup({
    label: new FormControl({ value: '', disabled: false }, [Validators.pattern('^[a-zA-Z0-9 ]+$')]),
    required: new FormControl(),
  });

  constructor(
    public dialogRef: MatDialogRef<AddFieldDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  submit() {
    const content = this.form.getRawValue();
    this.dialogRef.close({
      key: content.label?.replace(' ', '-').toLowerCase(),
      ...content,
    });
  }

  cancel() {
    this.dialogRef.close();
  }

}
