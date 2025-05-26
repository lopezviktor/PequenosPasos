import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Siesta } from '@models/siesta.model';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-siesta-table',
  standalone: true,
  imports: [
    CommonModule, 
    TableModule, 
    ButtonModule, 
    ConfirmDialogModule,
    CalendarModule,
    FormsModule,
    DropdownModule,
    TranslateModule
  ],
  templateUrl: './siesta-table.component.html',
  styleUrl: './siesta-table.component.scss',
  providers: [MessageService, ConfirmationService]
})
export class SiestaTableComponent {
  // Filtros
  ninoFiltro: number | null = null;
  educadorFiltro: number | null = null;
  fechaFiltro: Date | null = null;

  // Listas únicas
  ninosUnicos: { id: number; nombre: string }[] = [];
  educadoresUnicos: { id: number; nombre: string }[] = [];

  // Siestas (setter para procesar listas únicas)
  private _siestas: Siesta[] = [];
  @Input() set siestas(value: Siesta[]) {
    this._siestas = value;
    this._siestas.sort((a, b) =>
      new Date(b.inicioSiesta).getTime() - new Date(a.inicioSiesta).getTime()
    );

    const ninosMap = new Map<number, string>();
    const educadoresMap = new Map<number, string>();

    value.forEach(s => {
      if (s.nino?.id !== undefined) {
        ninosMap.set(s.nino.id, `${s.nino.nombre} ${s.nino.apellidos}`);
      }
      if (s.educador?.id !== undefined) {
        educadoresMap.set(s.educador.id, `${s.educador.nombre} ${s.educador.apellidos}`);
      }
    });

    this.ninosUnicos = Array.from(ninosMap.entries())
      .map(([id, nombre]) => ({ id, nombre }))
      .sort((a, b) => a.nombre.localeCompare(b.nombre));

    this.educadoresUnicos = Array.from(educadoresMap.entries())
      .map(([id, nombre]) => ({ id, nombre }))
      .sort((a, b) => a.nombre.localeCompare(b.nombre));
  }
  get siestas(): Siesta[] {
    return this._siestas;
  }

  // Salidas
  @Output() editar = new EventEmitter<Siesta>();
  @Output() eliminado = new EventEmitter<void>();

  constructor(
    private siestaService: SiestaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  // Getter para aplicar filtros
  get siestasFiltradas(): Siesta[] {
    return this._siestas.filter(s =>
      (!this.ninoFiltro || s.nino?.id === this.ninoFiltro) &&
      (!this.educadorFiltro || s.educador?.id === this.educadorFiltro) &&
      (!this.fechaFiltro || (
        s.inicioSiesta &&
        new Date(s.inicioSiesta).toDateString() === this.fechaFiltro.toDateString()
      ))
    );
  }

  // Limpiar filtros
  limpiarFiltros(): void {
    this.ninoFiltro = null;
    this.educadorFiltro = null;
    this.fechaFiltro = null;
  }

  confirmarEliminacion(siesta: Siesta) {
    this.confirmationService.confirm({
      message: '¿Estás seguro de que deseas eliminar esta siesta?',
      acceptLabel: 'Sí',
      rejectLabel: 'No',
      accept: () => this.eliminarSiesta(siesta.id!)
    });
  }

  eliminarSiesta(id: number) {
    this.siestaService.delete(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Eliminada', detail: 'Siesta eliminada correctamente' });
        this.eliminado.emit();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar la siesta' });
      }
    });
  }

  editarSiesta(siesta: Siesta) {
    this.editar.emit(siesta);
  }
}
