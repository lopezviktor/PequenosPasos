import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
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

export class ActividadFormComponent implements OnInit, OnChanges {
  @Input() actividadEditando?: Actividad;
  @Output() actividadGuardada = new EventEmitter<void>();
  @Output() formularioCerrado = new EventEmitter<void>();

  form!: FormGroup;
  educadores: Educator[] = [];
  clases: Clase[] = [];
  isEditing = false;

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
      clase: [null, Validators.required]
    });

    this.loadEducadores();
    this.loadClases();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['actividadEditando'] && this.actividadEditando) {
      this.isEditing = true;
      this.form.patchValue({
        nombre: this.actividadEditando.nombre,
        descripcion: this.actividadEditando.descripcion,
        fecha: this.actividadEditando.fecha ? new Date(this.actividadEditando.fecha) : null,
        educador: this.actividadEditando.educador,
        clase: this.actividadEditando.clase
      });
    }

    if (changes['actividadEditando'] && !this.actividadEditando) {
      this.isEditing = false;
      this.form.reset();
    }
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

  onSubmit(): void {
    if (this.form.invalid) return;

    const actividadData = this.form.value;

    const actividadSimplificada = {
      nombre: actividadData.nombre,
      descripcion: actividadData.descripcion,
      fecha: actividadData.fecha,
      educador: actividadData.educador,
      clase: actividadData.clase?.id || null
    };

    this.actividadService.create(actividadSimplificada).subscribe({
      next: (response) => {
        const actividadId: number = response.actividadId!;
        const claseId = actividadData.clase?.id;

        if (claseId) {
          const payload = {
            claseId: claseId,
            actividad: {
              actividadId: actividadId
            }
          };
          console.log("Datos para registrar actividad en clase:", payload);
          this.actividadService.registrarActividadPorClase(claseId!, actividadId!).subscribe({
            next: () => {
              this.messageService.add({ severity: 'success', summary: 'Registrada', detail: 'Actividad registrada para la clase' });
              this.actividadGuardada.emit();
              this.cancelar();
            },
            error: (err) => {
              console.error("Error al registrar actividad para la clase:", err);
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al registrar la actividad para la clase' });
            }
          });
        }
      },
      error: (err) => {
        console.error("Error al crear la actividad:", err);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error al crear la actividad' });
      }
    });
  }

  cancelar(): void {
    this.formularioCerrado.emit();
  }
}
