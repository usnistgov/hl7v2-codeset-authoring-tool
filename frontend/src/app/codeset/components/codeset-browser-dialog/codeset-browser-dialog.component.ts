import { Component, Inject, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { CodesetService } from '../../services/codeset.service';
import { Papa } from 'ngx-papaparse';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { TreeTable, TreeTableModule } from 'primeng/treetable';
import { ICodesetDescriptor } from '../../models/codeset';
import { map } from 'rxjs';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-codeset-browser-dialog',
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
    FaIconComponent

  ],
  templateUrl: './codeset-browser-dialog.component.html',
  styleUrl: './codeset-browser-dialog.component.scss'
})
export class CodesetBrowserDialogComponent {

  selectedCodesets: ICodesetDescriptor[] = [];
  codesets: ICodesetDescriptor[] = [];

  dialogData: any;
  filterText: string = '';
  @ViewChild('tt') treeTable!: TreeTable;
  columns = ['name'];
  constructor(
    public dialogRef: MatDialogRef<CodesetBrowserDialogComponent>,
    private codesetService: CodesetService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.dialogData = data;
    this.selectedCodesets = data.selectedCodesets
    this.getCodesetList();

  }



  getCodesetList() {
    this.codesetService.getCodesetList().pipe(
      map((values) => {
        this.codesets = values;
        console.log(this.codesets)
      })
    ).subscribe();
  }
  filterTextChanged(text: string) {
    this.treeTable.filter(text, 'label', 'contains');
  }
  clearSelection() {
    this.selectedCodesets = [];
  }
  clear(i: number) {
    if (Array.isArray(this.selectedCodesets)) {
      this.selectedCodesets.splice(i, 1);
    }
  }



  submit() {
    console.log(this.selectedCodesets)
    this.dialogRef.close(this.selectedCodesets);
  }
}
