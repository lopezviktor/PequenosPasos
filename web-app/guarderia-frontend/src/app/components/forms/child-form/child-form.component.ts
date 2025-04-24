import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { Child } from '@models/child.model';
import { Clase } from '@models/clase.model';
import { ClassroomService } from '@services/classroom/classroom.service';

@Component({
  selector: 'app-child-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    DropdownModule,
    CalendarModule
  ],
  templateUrl: './child-form.component.html',
  styleUrl: './child-form.component.scss'
})

export class ChildFormComponent implements OnChanges {
  @Output() submitChild = new EventEmitter<any>();
  @Input() child?: Child;
  @Input() clases: Clase[] = [];

  childForm: FormGroup;
  clasesDisponibles: Clase[] = [];

  constructor(private fb: FormBuilder, private classroomService: ClassroomService) {
    this.childForm = this.fb.group({
      id: [null],
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      primerDia: [''],
      alergias: [''],
      condicionesMedicas: [''],
      fotoUrl: [''],
      clase: ['', Validators.required]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['child'] && this.child) {
      const patch: any = {
        ...this.child,
        clase: typeof this.child.clase === 'object' && this.child.clase !== null
          ? (this.child.clase.id ?? this.child.clase.nombre ?? '')
          : this.child.clase ?? ''
      };
      this.childForm.patchValue(patch);
    }
    this.classroomService.getClases().subscribe({
      next: (clases) => {
        this.clasesDisponibles = clases;
      },
      error: (err) => {
        console.error('Error al cargar las clases:', err);
      }
    });
  }

  onSubmit() {
    if (this.childForm.valid) {
      const formValue = { ...this.childForm.value };
      formValue.clase = { id: formValue.clase };
  
      this.submitChild.emit(formValue);
    } else {
      this.childForm.markAllAsTouched();
    }
  }
}