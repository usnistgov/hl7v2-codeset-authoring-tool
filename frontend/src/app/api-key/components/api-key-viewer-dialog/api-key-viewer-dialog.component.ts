import { Component, Inject, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { Papa } from 'ngx-papaparse';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { TreeTable, TreeTableModule } from 'primeng/treetable';
import { map } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { InputCopyComponent } from '../../../codeset/components/input-copy/input-copy.component';

@Component({
  selector: 'app-api-key-viewer-dialog',
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
    TreeTableModule,
    FaIconComponent,
    InputCopyComponent

  ],
  templateUrl: './api-key-viewer-dialog.component.html',
  styleUrl: './api-key-viewer-dialog.component.scss'
})
export class ApiKeyViewerDialogComponent {


  dialogData: any;
  constructor(
    public dialogRef: MatDialogRef<ApiKeyViewerDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.dialogData = data;
  }


}
