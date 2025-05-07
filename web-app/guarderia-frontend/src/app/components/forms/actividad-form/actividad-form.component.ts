import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';

import { Actividad } from '@models/actividad.model';
import { Educator } from '@models/educator.model';
import { Child } from '@models/child.model';
import { Clase } from '@models/clase.model';

import { ChildService } from '@services/child/child.service';
import { ActividadService } from '@services/actividad/actividad.service';
import { EducatorService } from '@services/educator/educator.service';
import { ClassroomService } from '@services/classroom/classroom.service';

import { MessageService } from 'primeng/api';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-actividad-form',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    CalendarModule,
    DropdownModule,
    MultiSelectModule,
    ButtonModule
  ],
  templateUrl: './actividad-form.component.html',
  styleUrls: ['./actividad-form.component.scss'],
  providers: [MessageService]
})

export class ActividadFormComponent implements OnInit {
  @Input() actividadEditando?: Actividad;
  @Output() actividadGuardada = new EventEmitter<void>();
  @Output() formularioCerrado = new EventEmitter<void>();

  form!: FormGroup;
  educadores: Educator[] = [];
  ninos: Child[] = [];
  clases: Clase[] = [];
  isEditing = false;
  modoRegistro: 'clase' | 'individual' = 'individual'; // Nuevo modo de registro

  constructor(
    private fb: FormBuilder,
    private actividadService: ActividadService,
    private childService: ChildService,
    private educatorService: EducatorService,
    private claseService: ClassroomService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: ['', Validators.required],
      fecha: [null, Validators.required],
      educador: [null, Validators.required],
      clase: [null],
      ninos: [[]],
      modoRegistro: ['individual'] 
    });

    this.loadNinos();
    this.loadEducadores();
    this.loadClases();

    if (this.actividadEditando) {
      this.isEditing = true;
      this.form.patchValue({
        nombre: this.actividadEditando.nombre,
        descripcion: this.actividadEditando.descripcion,
        fecha: this.actividadEditando?.fecha ? new Date(this.actividadEditando.fecha) : null,
        educador: this.actividadEditando.educador,
        clase: this.actividadEditando.clase,
        ninos: this.actividadEditando.ninos || []
      });
    }
  }

  cambiarModo(modo: 'clase' | 'individual'): void {
    this.modoRegistro = modo;
    this.form.patchValue({ clase: null, ninos: [] }); // Limpiar selección previa

    if (modo === 'clase') {
      this.loadClases(); // Cargar las clases disponibles
      this.ninos = [];   // Limpiar la lista de niños
    } else {
      this.loadNinos();  // Cargar todos los niños
      this.clases = [];  // Limpiar la lista de clases
    }
  }

  onClaseSeleccionada(clase: any): void {
    const claseId = clase?.id || clase;
    this.loadNinosPorClase(claseId);
  }

  loadNinos(): void {
    this.childService.getAllChildren().subscribe({
      next: (ninos) => this.ninos = ninos,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar los niños' })
    });
  }

  loadEducadores(): void {
    this.educatorService.getEducators().subscribe({
      next: (educadores) => this.educadores = educadores,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar los educadores' })
    });
  }

  loadClases(): void {
    this.claseService.getClases().subscribe({
      next: (clases) => this.clases = clases,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar las clases' })
    });
  }

  loadNinosPorClase(claseId: number): void {
    this.childService.getChildrenByClass(claseId).subscribe({
      next: (ninos) => this.ninos = ninos,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar los niños de la clase' })
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
  
    const actividadData = this.form.value;
    console.log("Datos enviados:", actividadData);
  
    if (this.isEditing && this.actividadEditando) {
      this.actividadService.update(this.actividadEditando.actividadId!, actividadData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Actualizada', detail: 'Actividad actualizada correctamente' });
          this.actividadGuardada.emit();
          this.cancelar();
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al actualizar la actividad' })
      });
    } else {
      if (this.modoRegistro === 'clase' && actividadData.clase) {
        // Extraer solo el ID de la clase
        const claseId = actividadData.clase.id;
        const actividadSimplificada = {
          nombre: actividadData.nombre,
          descripcion: actividadData.descripcion,
          fecha: actividadData.fecha,
          educador: actividadData.educador
        };
        // Crear el objeto con la estructura que el backend espera
        const requestData = {
          claseId: claseId,
          actividad: actividadSimplificada
        };
        console.log("Datos enviados para la clase:", requestData);

        this.actividadService.registrarActividadPorClase(claseId, actividadSimplificada).subscribe({
          next: (response) => {
            console.log("Respuesta del backend:", response);
            this.messageService.add({ severity: 'success', summary: 'Registrada', detail: 'Actividad registrada para la clase' });
            this.actividadGuardada.emit();
            this.cancelar();
          },
          error: (err) => {
            console.error("Error al registrar actividad para la clase:", err);
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al registrar la actividad para la clase' });
          }
        });

      } else if (this.modoRegistro === 'individual' && actividadData.ninos?.length) {
        // Registrar actividad para niños seleccionados
        const ninoIds = actividadData.ninos.map((nino: Child) => nino.id);
        console.log("Niños seleccionados:", ninoIds);
        this.actividadService.registrarActividadIndividual(actividadData.id, ninoIds).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Creada', detail: 'Actividad creada para los niños seleccionados' });
            this.actividadGuardada.emit();
            this.cancelar();
          },
          error: (err) => {
            console.error("Error al registrar actividad individual:", err);
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear la actividad para los niños' });
          }
        });
      } else {
        this.messageService.add({ severity: 'warn', summary: 'Atención', detail: 'Por favor, selecciona una clase o al menos un niño' });
      }
    }
  }

  cancelar(): void {
    this.formularioCerrado.emit();
  }
}
