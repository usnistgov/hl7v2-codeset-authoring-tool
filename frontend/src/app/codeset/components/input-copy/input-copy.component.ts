import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogTitle, MatDialogActions, MatDialogClose } from "@angular/material/dialog";
import { ICodesetVersion, ICodesetVersionCode } from '../../models/codeset';
import { Guid } from 'guid-typescript';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-input-copy',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    TableModule,
    MultiSelectModule,
    DropdownModule,
    CommonModule,
    NgbDropdownModule,
    FaIconComponent,

  ],
  templateUrl: './input-copy.component.html',
  styleUrl: './input-copy.component.scss'
})
export class InputCopyComponent {
  @Input()
  label!: string;
  @Input()
  type!: string;
  @Input()
  value!: string;
  copied = false;

  constructor() { }

  copy() {
    window.navigator['clipboard'].writeText(this.value);
    this.copied = true;
    setTimeout(() => {
      this.copied = false;
      this.type = 'text';
    }, 600);
  }


}
