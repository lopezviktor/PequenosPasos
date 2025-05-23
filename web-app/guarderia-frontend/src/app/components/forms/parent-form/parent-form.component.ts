import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { Parent } from '@models/parent.model';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-parent-form',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    TranslateModule
  ],
  templateUrl: './parent-form.component.html',
  styleUrl: './parent-form.component.scss'
})

export class ParentFormComponent implements OnChanges {
  @Output() submitParent = new EventEmitter<any>();
  @Input() parent?: Parent;

  parentForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.parentForm = this.fb.group({
      id: [null],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      tipoUsuario: [{ value: 'PADRE', disabled: true }]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['parent']) {
      if (this.parent) {
        this.parentForm.patchValue(this.parent);
      } else {
        this.parentForm.reset({
          id: null,
          nombre: '',
          apellidos: '',
          email: '',
          telefono: '',
          password: '',
          tipoUsuario: { value: 'PADRE', disabled: true }
        });
      }
    }
  }

  onSubmit() {
    if (this.parentForm.valid) {
      this.submitParent.emit(this.parentForm.getRawValue());
    } else {
      this.parentForm.markAllAsTouched();
    }
  }
}