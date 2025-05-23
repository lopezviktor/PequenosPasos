import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { Asistencia } from '@models/asistencia.model';
import { MessageService } from 'primeng/api';
import { ChildService } from '@services/child/child.service';
import { EducatorService } from '@services/educator/educator.service';
import { ParentService } from '@services/parent/parent.service';
import { Child } from '@models/child.model';
import { Educator } from '@models/educator.model';
import { Parent } from '@models/parent.model';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';


@Component({
  selector: 'app-asistencia-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    DropdownModule,
    CalendarModule,
    ButtonModule,
    TranslateModule
  ],
  templateUrl: './asistencia-form.component.html',
  styleUrls: ['./asistencia-form.component.scss']
})

export class AsistenciaFormComponent implements OnInit, OnChanges {
  @Input() asistenciaEditando: Asistencia | null = null; // Para editar
  @Output() formularioCerrado = new EventEmitter<void>();
  @Output() asistenciaGuardada = new EventEmitter<void>();
  form: FormGroup;
  ninos: Child[] = [];
  educadores: Educator[] = [];
  padresEntrega: Parent[] = [];
  padresRecoge: Parent[] = [];
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private asistenciaService: AsistenciaService,
    private messageService: MessageService,
    private childService: ChildService,  // Asegúrate de importar el servicio para obtener los niños
    private padreService: ParentService, // Asegúrate de importar el servicio para obtener los padres
    private educatorService: EducatorService,
  ) {
    this.form = this.fb.group({
      nino: [null, Validators.required],
      educadorRecibe: [null, Validators.required],
      horaEntrada: [null, Validators.required],
      horaSalida: [null],
      educadorEntrega: [null],
      padreEntrega: [null],
      padreRecoge: [null],
    });
   }

  ngOnInit(): void {
    console.log('AsistenciaFormComponent inicializado');

    // Cargar los niños, educadores y padres
    this.loadNinos();
    this.loadEducadores();
    
    // Escuchar el cambio en el niño seleccionado
    this.form.get('nino')?.valueChanges.subscribe(nino => {
      const ninoId = typeof nino === 'object' ? nino.id : nino;
      if (ninoId) {
        this.loadPadres(ninoId);
      }
    });
  }

  // Cargar lista de niños
  loadNinos() {
    this.childService.getAllChildren().subscribe(ninos => {
      this.ninos = ninos;
    });
  }

  // Cargar lista de educadores
  loadEducadores() {
    this.educatorService.getEducators().subscribe(educadores => {
      console.log('Educadores cargados:', educadores);  // 👈
      this.educadores = educadores;
    });
  }

  // Cargar lista de padres, pero solo para el niño seleccionado
  loadPadres(ninoId: number) {
    this.padreService.getPadresByNino(ninoId).subscribe(padres => {
      console.log('Padres cargados:', padres);  // 👈
      const padresConNombreCompleto = padres.map(p => ({
        ...p,
        nombreCompleto: `${p.nombre} ${p.apellidos}`
      }));
      this.padresEntrega = padresConNombreCompleto;
      this.padresRecoge = padresConNombreCompleto;
    });
  }

  // Método para ajustar la zona horaria local
  ajustarZonaHorariaLocal(date: Date): Date {
    const offset = date.getTimezoneOffset();
    return new Date(date.getTime() - offset * 60 * 1000);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['asistenciaEditando'] && changes['asistenciaEditando'].currentValue) {
      this.isEditing = true;
      const asistencia = changes['asistenciaEditando'].currentValue as Asistencia;

      if (asistencia.nino?.id) {
        this.padreService.getPadresByNino(asistencia.nino.id).subscribe(padres => {
          const padresConNombreCompleto = padres.map(p => ({
            ...p,
            nombreCompleto: `${p.nombre} ${p.apellidos}`
          }));
          this.padresEntrega = padresConNombreCompleto;
          this.padresRecoge = padresConNombreCompleto;

          this.form.patchValue({
            nino: asistencia.nino,
            padreEntrega: asistencia.padreEntrega,
            educadorRecibe: asistencia.educadorRecibe,
            horaEntrada: new Date(asistencia.horaEntrada),
            educadorEntrega: asistencia.educadorEntrega,
            padreRecoge: asistencia.padreRecoge,
            horaSalida: asistencia.horaSalida ? new Date(asistencia.horaSalida) : null,
          });
        });
      }
    }
  }

  // Enviar formulario
  onSubmit() {
    if (this.form.valid) {
      const formData = { ...this.form.value };

      formData.horaEntrada = this.ajustarZonaHorariaLocal(formData.horaEntrada);
      formData.horaSalida = this.ajustarZonaHorariaLocal(formData.horaSalida);

      if (this.isEditing && this.asistenciaEditando) {
        // Actualizar asistencia
        this.asistenciaService.updateAsistencia(this.asistenciaEditando.id, formData).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Asistencia actualizada' });
            this.formularioCerrado.emit();
            this.asistenciaGuardada.emit();
          },
          error: (err) => {
            this.messageService.add({ 
              severity: 'error', 
              summary: 'Error al actualizar asistencia', 
              detail: err?.error?.message || 'Ocurrió un error inesperado',
              life: 5000
            });
          }
        });
      } else {
        // Crear nueva asistencia
        this.asistenciaService.createAsistencia(formData).subscribe(() => {
          this.messageService.add({ severity: 'success', summary: 'Asistencia guardada' });
          this.formularioCerrado.emit();
          this.asistenciaGuardada.emit();
        });
      }
    }
  }
}