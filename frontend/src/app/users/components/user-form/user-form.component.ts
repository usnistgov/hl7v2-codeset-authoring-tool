import { Component, EventEmitter, input, Input, Output } from '@angular/core';

import { FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';


@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.scss'
})
export class UserFormComponent {
  @Input() action: string = 'create';
  @Output()
  submitForm: EventEmitter<any> = new EventEmitter<any>();

  @Input() form!: FormGroup;
  Validators = Validators;


  constructor(

  ) {

  }
  submit() {
    this.submitForm.emit(this.form.value);
  }

}
