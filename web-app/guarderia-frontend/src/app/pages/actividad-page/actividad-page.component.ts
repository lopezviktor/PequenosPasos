import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActividadConDetalles } from '@models/actividad.model';
import { ActividadService } from '@services/actividad/actividad.service';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ActividadCardComponent } from '@components/actividad-card/actividad-card.component';
import { ActividadFormComponent } from '@components/forms/actividad-form/actividad-form.component';

@Component({
  selector: 'app-actividad-page',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    DialogModule,
    ActividadCardComponent,
    ActividadFormComponent
  ],
  templateUrl: './actividad-page.component.html',
  styleUrls: ['./actividad-page.component.scss'],
  providers: [MessageService]
})
export class ActividadPageComponent implements OnInit {

  actividades: ActividadConDetalles[] = [];
  actividadSeleccionada?: ActividadConDetalles;
  mostrarFormulario = false;

  constructor(
    private actividadService: ActividadService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.cargarActividades();
  }

  cargarActividades(): void {
    this.actividadService.getActividades().subscribe({
      next: (actividades) => this.actividades = actividades,
      error: () => this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudieron cargar las actividades' })
    });
  }

  crearActividad(): void {
    this.actividadSeleccionada = undefined;
    this.mostrarFormulario = true;
  }

  editarActividad(actividad: ActividadConDetalles): void {
    this.actividadSeleccionada = actividad;
    this.mostrarFormulario = true;
  }

  eliminarActividad(id: number): void {
    if (!id) {
        console.error('ID no válido para eliminar actividad:', id);
        return;
    }
    this.actividadService.deleteActividad(id).subscribe(
        () => {
            console.log('Actividad eliminada correctamente:', id);
            this.cargarActividades();
        },
        (error) => {
            console.error('Error al eliminar la actividad:', error);
        }
    );
}

  onGuardar(): void {
    this.cargarActividades();
    this.mostrarFormulario = false;
  }

  onCancelar(): void {
    this.mostrarFormulario = false;
  }
}
