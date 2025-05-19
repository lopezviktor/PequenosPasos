import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Higiene } from '@models/higiene.model';
import { HigieneService } from '@services/higiene/higiene.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-higiene-table',
  standalone: true,
  imports: [
    CommonModule, 
    TableModule, 
    ButtonModule, 
    ConfirmDialogModule,
    CalendarModule,
    FormsModule,
    DropdownModule
  ],
  templateUrl: './higiene-table.component.html',
  styleUrl: './higiene-table.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class HigieneTableComponent implements OnInit {
  higienes: Higiene[] = [];
  @Output() editar = new EventEmitter<Higiene>();

  // Filtros
  ninoFiltro: number | null = null;
  educadorFiltro: number | null = null;
  fechaFiltro: Date | null = null;
  estadoFiltro: 'NORMAL' | 'ESTREÑIDO' | 'SUELTO' | null = null;

  estadosDisponibles = [
    { label: 'Normal', value: 'NORMAL' },
    { label: 'Estreñido', value: 'ESTREÑIDO' },
    { label: 'Suelto', value: 'SUELTO' }
  ];

  ninosUnicos: { id: number; nombre: string }[] = [];
  educadoresUnicos: { id: number; nombre: string }[] = [];

  constructor(
    private higieneService: HigieneService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadHigienes();
  }

  loadHigienes(): void {
    this.higieneService.getAll().subscribe(h => {
      this.higienes = h.sort((a, b) =>
        new Date(b.fechaHora).getTime() - new Date(a.fechaHora).getTime()
      );

      const ninosMap = new Map<number, string>();
      const educadoresMap = new Map<number, string>();

      h.forEach(registro => {
        if (registro.nino?.id !== undefined) {
          ninosMap.set(registro.nino.id, `${registro.nino.nombre} ${registro.nino.apellidos}`);
        }
        if (registro.educador?.id !== undefined) {
          educadoresMap.set(registro.educador.id, `${registro.educador.nombre} ${registro.educador.apellidos}`);
        }
      });

      this.ninosUnicos = Array.from(ninosMap.entries())
        .map(([id, nombre]) => ({ id, nombre }))
        .sort((a, b) => a.nombre.localeCompare(b.nombre));

      this.educadoresUnicos = Array.from(educadoresMap.entries())
        .map(([id, nombre]) => ({ id, nombre }))
        .sort((a, b) => a.nombre.localeCompare(b.nombre));
    });
  }

  get higienesFiltradas(): Higiene[] {
    return this.higienes.filter(h =>
      (!this.ninoFiltro || h.nino?.id === this.ninoFiltro) &&
      (!this.educadorFiltro || h.educador?.id === this.educadorFiltro) &&
      (!this.fechaFiltro || (
        h.fechaHora &&
        new Date(h.fechaHora).toDateString() === this.fechaFiltro.toDateString()
      )) &&
      (!this.estadoFiltro || h.estado === this.estadoFiltro)
    );
  }

  limpiarFiltros(): void {
    this.ninoFiltro = null;
    this.educadorFiltro = null;
    this.fechaFiltro = null;
    this.estadoFiltro = null;
  }

  editarHigiene(higiene: Higiene) {
    this.editar.emit(higiene);
  }

  eliminarHigiene(id: number): void {
    this.confirmationService.confirm({
      message: '¿Estás seguro de que deseas eliminar este registro de higiene?',
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.higieneService.delete(id).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Eliminado', detail: 'Registro eliminado correctamente' });
            this.loadHigienes();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el registro' });
          }
        });
      }
    });
  }
}
