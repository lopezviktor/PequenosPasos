import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-parent-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule
  ],
  templateUrl: './parent-form.component.html',
  styleUrl: './parent-form.component.scss'
})
export class ParentFormComponent {
  @Output() submitParent = new EventEmitter<any>();

  parentForm;

  constructor(private fb: FormBuilder) {
    this.parentForm = this.fb.group({
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit() {
    if (this.parentForm.valid) {
      this.submitParent.emit(this.parentForm.value);
    } else {
      this.parentForm.markAllAsTouched();
    }
  }
}
