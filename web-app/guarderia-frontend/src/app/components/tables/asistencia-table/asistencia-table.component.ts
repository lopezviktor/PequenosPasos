import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { Asistencia } from '@models/asistencia.model';
import { MessageService, ConfirmationService } from 'primeng/api';
import { NombreCompletoPipe } from '@shared/pipes/nombre-completo.pipe';
import { ConfirmDialogModule } from 'primeng/confirmdialog';


@Component({
  selector: 'app-asistencia-table',
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    DialogModule,
    FormsModule,
    ReactiveFormsModule,
    NombreCompletoPipe,
    ConfirmDialogModule
  ],
  templateUrl: './asistencia-table.component.html',
  styleUrl: './asistencia-table.component.scss',
  providers: [ConfirmationService]
})
export class AsistenciaTableComponent implements OnInit {

  @Output() editar = new EventEmitter<Asistencia>();

  asistencias: Asistencia[] = [];

  constructor(
    private asistenciaService: AsistenciaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.loadAsistencias();
  }

  loadAsistencias(): void {
    this.asistenciaService.getAsistencias().subscribe(
      (asistencias) => {
        this.asistencias = asistencias;
      },
      (error) => {
        console.error('Error loading asistencias', error);
      }
    );
  }

  onEdit(asistencia: Asistencia): void {
    this.editar.emit(asistencia);
  }

  onDelete(id: number): void {
    this.confirmationService.confirm({
      message: '¿Estás seguro de que quieres eliminar esta asistencia?',
      acceptLabel: 'Sí',
      rejectLabel: 'No',
      accept: () => {
        this.asistenciaService.deleteAsistencia(id).subscribe(() => {
          this.loadAsistencias();
          this.messageService.add({ severity: 'success', summary: 'Asistencia eliminada' });
        });
      }
    });
  }
}
