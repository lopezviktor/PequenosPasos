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
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateService } from '@ngx-translate/core';

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
    ConfirmDialogModule,
    DropdownModule,
    CalendarModule,
    TranslateModule
  ],
  templateUrl: './asistencia-table.component.html',
  styleUrl: './asistencia-table.component.scss',
  providers: [ConfirmationService]
})
export class AsistenciaTableComponent implements OnInit {

  @Output() editar = new EventEmitter<Asistencia>();

  asistencias: Asistencia[] = [];
  ninoFiltro: number | null = null;
  educadorFiltro: number | null = null;
  fechaFiltro: Date | null = null;
  ninosUnicos: { id: number; nombre: string }[] = [];
  educadoresUnicos: { id: number; nombre: string }[] = [];

  constructor(
    private asistenciaService: AsistenciaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private translate: TranslateService
  ) { }

  ngOnInit(): void {
    this.loadAsistencias();
  }

  loadAsistencias(): void {
    this.asistenciaService.getAsistencias().subscribe(
      (asistencias) => {
        this.asistencias = asistencias.sort((a, b) =>
          new Date(b.horaEntrada).getTime() - new Date(a.horaEntrada).getTime()
        );

        const ninosMap = new Map<number, string>();
        const educadoresMap = new Map<number, string>();

        asistencias.forEach(a => {
          if (a.nino && !ninosMap.has(a.nino.id)) {
            ninosMap.set(a.nino.id, `${a.nino.nombre} ${a.nino.apellidos}`);
          }
          if (a.educadorRecibe && !educadoresMap.has(a.educadorRecibe.id)) {
            educadoresMap.set(a.educadorRecibe.id, `${a.educadorRecibe.nombre} ${a.educadorRecibe.apellidos}`);
          }
        });

        this.ninosUnicos = Array.from(ninosMap.entries())
          .map(([id, nombre]) => ({ id, nombre }))
          .sort((a, b) => a.nombre.localeCompare(b.nombre));

        this.educadoresUnicos = Array.from(educadoresMap.entries())
          .map(([id, nombre]) => ({ id, nombre }))
          .sort((a, b) => a.nombre.localeCompare(b.nombre));
      },
      (error) => {
        console.error('Error loading asistencias', error);
      }
    );
  }

  get asistenciasFiltradas(): Asistencia[] {
    return this.asistencias.filter(a =>
      (!this.ninoFiltro || a.nino?.id === this.ninoFiltro) &&
      (!this.educadorFiltro || a.educadorRecibe?.id === this.educadorFiltro) &&
      (!this.fechaFiltro || (
        a.horaEntrada &&
        new Date(a.horaEntrada).toDateString() === this.fechaFiltro.toDateString()
      ))
    );
  }

  limpiarFiltros(): void {
    this.ninoFiltro = null;
    this.educadorFiltro = null;
    this.fechaFiltro = null;
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
