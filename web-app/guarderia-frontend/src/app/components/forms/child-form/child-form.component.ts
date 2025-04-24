

import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { Child } from '@models/child.model';

@Component({
  selector: 'app-child-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    CalendarModule
  ],
  templateUrl: './child-form.component.html',
  styleUrl: './child-form.component.scss'
})
export class ChildFormComponent implements OnChanges {
  @Output() submitChild = new EventEmitter<any>();
  @Input() child?: Child;

  childForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.childForm = this.fb.group({
      id: [null],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      clase: ['', Validators.required],
      imagenUrl: ['']
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['child'] && this.child) {
      this.childForm.patchValue(this.child);
    }
  }

  onSubmit() {
    if (this.childForm.valid) {
      this.submitChild.emit(this.childForm.value);
    } else {
      this.childForm.markAllAsTouched();
    }
  }
}