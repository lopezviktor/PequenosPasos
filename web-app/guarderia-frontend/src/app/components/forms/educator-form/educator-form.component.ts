import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Educator } from '@models/educator.model';

@Component({
  selector: 'app-educator-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule
  ],
  templateUrl: './educator-form.component.html',
  styleUrl: './educator-form.component.scss'
})
export class EducatorFormComponent implements OnChanges {
  @Output() submitEducator = new EventEmitter<any>();
  @Input() educator?: Educator;

  educatorForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.educatorForm = this.fb.group({
      id: [null],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      tipoUsuario: [{ value: 'EDUCADOR', disabled: true }]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['educator'] && this.educator) {
      this.educatorForm.patchValue(this.educator);
    }
  }

  onSubmit() {
    if (this.educatorForm.valid) {
      this.submitEducator.emit(this.educatorForm.getRawValue());
    } else {
      this.educatorForm.markAllAsTouched();
    }
  }
}
