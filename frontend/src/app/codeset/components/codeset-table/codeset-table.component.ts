import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormsModule, NgForm, ReactiveFormsModule, Validators } from '@angular/forms';
import { ICodesetVersion, ICodesetVersionCode } from '../../models/codeset';
import { Guid } from 'guid-typescript';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownChangeEvent, DropdownModule } from 'primeng/dropdown';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-codeset-table',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TableModule,
    MultiSelectModule,
    DropdownModule,
    CommonModule,
    NgbDropdownModule,
    FaIconComponent,

  ],
  templateUrl: './codeset-table.component.html',
  styleUrl: './codeset-table.component.scss'
})
export class CodesetTableComponent {

  _codeSetVersion!: ICodesetVersion;
  codeSystems: string[] = [];
  @ViewChild('form') form!: NgForm;

  @Input()
  set codeSetVersion(codeSetVersion: ICodesetVersion) {
    this._codeSetVersion = codeSetVersion;
    this.codeSystems = this.getUniqueCodeSystems(this._codeSetVersion.codes ? this._codeSetVersion.codes : []);
    this._codeSetVersion.codeSystems = this.codeSystems;
    this.codeSystemOptions = this.getCodeSystemOptions();
    this.selectedCodes = [];
  }
  get codeSetVersion() {
    return this._codeSetVersion;
  }


  selectedCodes: ICodesetVersionCode[] = [];

  edit = {};

  temp: string | null = null;

  filteredCodeSystems: string[] = [];
  @Output()
  changes: EventEmitter<{ codes: ICodesetVersionCode[], valid: boolean | null }> = new EventEmitter<{ codes: ICodesetVersionCode[], valid: boolean | null }>();

  @Output()
  exportCSVEvent: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  importCSVEvent: EventEmitter<any> = new EventEmitter<any>();

  @Input()
  existingChangeReason: any[] = [];
  @Input()
  viewOnly: boolean = false;

  codeSystemOptions: any[] = [];
  @Input()
  cols: any[] = [];
  @Input()
  selectedColumns: any[] = [];
  editMap: any = {};

  codeUsageOptions = [
    { label: 'R', value: 'R' }, { label: 'P', value: 'P' }, { label: 'E', value: 'E' },
  ];
  ngOnInit() {
    this.editMap[this.codeSetVersion.id] = false;
    this.cols = this.selectedColumns;
  }
  constructor(private fb: FormBuilder) {

  }

  toggleEdit(id: string) {
    this.temp = null;
    const tempMap = this.editMap;
    this.editMap = {};
    this.editMap[id] = !tempMap[id];
  }

  addCodeSystem(targetId: string) {
    if (!this.codeSetVersion.codeSystems) {
      this.codeSetVersion.codeSystems = [];
      this.codeSystemOptions = [];
    }
    if (this.codeSetVersion.codeSystems.indexOf(this.temp as string) < 0) {
      this.codeSetVersion.codeSystems.push(this.temp as string);
      this.codeSystemOptions.push({ value: this.temp, label: this.temp });
    }
    this.toggleEdit(targetId);
  }

  filterCodeSystems(event: any) {
    this.filteredCodeSystems = this.codeSetVersion.codeSystems.filter((codeSystem: string) => {
      return codeSystem.toLowerCase().indexOf(event.query.toLowerCase()) === 0;
    });
  }

  getCodeSystemOptions() {
    return this.codeSystems.filter(c => c).map((codeSystem: string) => {
      return { label: codeSystem, value: codeSystem };
    });
  }

  deleteCodeSystem(codeSystem: string) {
    this.codeSetVersion.codeSystems = this.codeSetVersion.codeSystems.filter((codeSys: string) => {
      return codeSys != null && codeSystem.toLowerCase() !== codeSys.toLowerCase();
    });
    if (this.codeSetVersion.codes) {
      for (const code of this.codeSetVersion.codes) {
        if (code.system && code.system.toLowerCase() === codeSystem.toLowerCase()) {
          code.system = null;
        }
      }
    }

    this.codeSystemOptions = this.getCodeSystemOptions();
    this.updateAttribute('CODES', this.codeSetVersion.codes);
    this.updateAttribute('CODESYSTEM', this.codeSetVersion.codeSystems);
  }

  addCode() {
    if (!this.codeSetVersion.codes) {
      this.codeSetVersion.codes = [];
    }
    this.codeSetVersion.codes = [
      {
        id: Guid.create().toString(),
        code: '',
        description: '',
        system: '',
        usage: '',
        display: '',
        comments: '',
        pattern: '',
        hasPattern: false,
      },
      ...this.codeSetVersion.codes
    ]
    this.changeCodes();

  }

  applyUsage(usage: string) {
    for (const code of this.selectedCodes) {
      code.usage = usage;
    }
    this.updateAttribute('CODES', this.codeSetVersion.codes);
  }

  applyCodeSystem($event: DropdownChangeEvent) {
    for (const code of this.selectedCodes) {
      code.system = $event.value;
    }
  }

  changeCodes() {
    this.changes.emit({ codes: this.codeSetVersion.codes, valid: this.form.valid });
  }
  setHasPattern(code: ICodesetVersionCode) {
    code.hasPattern = true;
    code.pattern = '';
    // add pattern control to the form to make form validation work correctly
    this.form.control.addControl('pattern' + code.id, new FormControl('', { validators: Validators.required, updateOn: 'blur' }));
    this.changeCodes();
  }

  removeHasPattern(code: ICodesetVersionCode) {
    code.hasPattern = false;
    code.pattern = '';
    // remove pattern control to the form to make form validation work correctly
    this.form.control.removeControl('pattern' + code.id);
    this.changeCodes();
  }
  addCodeSystemFormCode(code: ICodesetVersionCode) {
    code.system = this.temp;
    this.addCodeSystem(code.id);
    this.changeCodes();
  }

  deleteCodes() {
    if (this.codeSetVersion.codes) {
      this.codeSetVersion.codes = this.codeSetVersion.codes.filter((x) => this.selectedCodes.indexOf(x) < 0);
      this.selectedCodes = [];
      this.changeCodes();
    }


  }

  updateAttribute(propertyType: string, value: any) {
    // change if we add other attributes
    // this.changes.emit(this.codeSetVersion.codes);

  }
  updateURl(value: string) {
    this.updateAttribute('URL', value);
  }

  importCSV() {
    this.importCSVEvent.emit(this.codeSetVersion);
  }
  exportCSV() {
    this.exportCSVEvent.emit(this.codeSetVersion);
  }
  getUniqueCodeSystems(codes: ICodesetVersionCode[]): string[] {
    const codeSystems = codes.map((code) => code.system);
    const uniqueCodeSystems = new Set(codeSystems);
    return Array.from(uniqueCodeSystems) as string[];
  }

  downloadExample() {
    const exampleCSV = 'data:text/csv;charset=utf-8,value,pattern,description,codeSystem,usage,comments\nvalue1,pattern1,description1,codeSystem1,P,comments1';
    const encodedUri = encodeURI(exampleCSV);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', 'example.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }

}
