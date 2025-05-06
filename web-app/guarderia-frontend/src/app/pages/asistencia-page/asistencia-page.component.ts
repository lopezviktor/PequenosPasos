import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AsistenciaTableComponent } from '@components/tables/asistencia-table/asistencia-table.component';
import { AsistenciaFormComponent } from '@components/forms/asistencia-form/asistencia-form.component';
import { Asistencia } from '@models/asistencia.model';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { ViewChild } from '@angular/core';

@Component({
  selector: 'app-asistencia-page',
  standalone: true,
  imports: [
    CommonModule,
    AsistenciaTableComponent,
    AsistenciaFormComponent,
    ButtonModule,
    DialogModule
  ],
  templateUrl: './asistencia-page.component.html',
  styleUrl: './asistencia-page.component.scss'
})
export class AsistenciaPageComponent {
  asistenciaSeleccionada: Asistencia | null = null;
  mostrarDialogo = false;
  @ViewChild(AsistenciaTableComponent) tabla!: AsistenciaTableComponent;

  nuevaAsistencia() {
    this.asistenciaSeleccionada = null;
    this.mostrarDialogo = true;
  }
  
  editarAsistencia(asistencia: Asistencia) {
    this.asistenciaSeleccionada = asistencia;
    this.mostrarDialogo = true;
  }
  
  limpiarFormulario() {
    this.mostrarDialogo = false;
  }

  recargarTabla() {
    this.tabla.loadAsistencias();
  }
}

