import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { CodesetService } from '../../services/codeset.service';
import { Papa } from 'ngx-papaparse';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-import-codes-dialog',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    TableModule,

  ],
  templateUrl: './import-codes-dialog.component.html',
  styleUrl: './import-codes-dialog.component.scss'
})
export class ImportCodesDialogComponent {
  fileToUpload: File | null = null;
  uploadStep = true;
  reviewStep = false;
  errorStep = false;
  errorMessage = '';
  codes: any[] = [];
  selectedCodes: any[] = [];

  constructor(
    public dialogRef: MatDialogRef<ImportCodesDialogComponent>,
    private codesetService: CodesetService,
    private papa: Papa
  ) { }

  onDragOver(event: Event) {
    event.preventDefault();
  }

  onDragLeave(event: Event) {
    event.preventDefault();
  }

  onFileDrop(event: DragEvent) {
    event.preventDefault();
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      this.fileToUpload = files.item(0);
      this.uploadFile(files.item(0) as Blob)
    }
  }

  fileInputChange(files: FileList | null) {
    if (files && files.length > 0) {
      this.fileToUpload = files.item(0);
    }
  }

  removeFile() {
    this.fileToUpload = null;
  }

  onFileSelect(event: any): void {
    const file = event.target.files[0];

    if (file) {
      this.uploadFile(file);
    }
  }

  uploadFile(blob: Blob) {
    const reader = new FileReader();
    reader.onload = (e) => {
      const csv = reader.result as string;
      this.papa.parse(csv, {
        header: true,
        skipEmptyLines: true,
        complete: (result) => {
          this.codes = result.data.map(
            (c: any) => {
              return {
                system: c.codeSystem,
                code: c.value,
                ...c
              }
            }
          );
          this.selectedCodes = this.codes;
          this.uploadStep = false;
          this.reviewStep = true;
          this.errorStep = false;
        },
        error: (error) => {
          console.error('Error parsing CSV:', error);
          console.log(error);
          this.errorMessage = error.message;
          this.uploadStep = false;
          this.reviewStep = false;
          this.errorStep = true;
        }
      });
    };

    reader.readAsText(blob);
  }


  submit() {
    this.dialogRef.close(this.selectedCodes);
  }
}
