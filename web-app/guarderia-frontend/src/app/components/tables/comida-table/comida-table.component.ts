import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comida } from '@models/comida.model';
import { ComidaService } from '@services/comida/comida.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-comida-table',
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
  templateUrl: './comida-table.component.html',
  styleUrl: './comida-table.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class ComidaTableComponent {

  ninoFiltro: number | null = null;
  educadorFiltro: number | null = null;
  fechaFiltro: Date | null = null;

  ninosUnicos: { id: number; nombre: string }[] = [];
  educadoresUnicos: { id: number; nombre: string }[] = [];

  private _comidas: Comida[] = [];
  @Input() set comidas(value: Comida[]) {
    this._comidas = value;
    this._comidas.sort((a, b) => new Date(b.horaComida).getTime() - new Date(a.horaComida).getTime());

    const ninosMap = new Map<number, string>();
    const educadoresMap = new Map<number, string>();

    value.forEach(c => {
      if (c.nino?.id !== undefined) ninosMap.set(c.nino.id, `${c.nino.nombre} ${c.nino.apellidos}`);
      if (c.educador?.id !== undefined) educadoresMap.set(c.educador.id, `${c.educador.nombre} ${c.educador.apellidos}`);
    });

    this.ninosUnicos = Array.from(ninosMap.entries()).map(([id, nombre]) => ({ id, nombre })).sort((a, b) => a.nombre.localeCompare(b.nombre));
    this.educadoresUnicos = Array.from(educadoresMap.entries()).map(([id, nombre]) => ({ id, nombre })).sort((a, b) => a.nombre.localeCompare(b.nombre));
  }
  get comidas(): Comida[] {
    return this._comidas;
  }
  @Output() editar = new EventEmitter<Comida>();
  @Output() eliminado = new EventEmitter<void>();

  constructor(
    private comidaService: ComidaService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  get comidasFiltradas(): Comida[] {
    return this._comidas.filter(c =>
      (!this.ninoFiltro || c.nino?.id === this.ninoFiltro) &&
      (!this.educadorFiltro || c.educador?.id === this.educadorFiltro) &&
      (!this.fechaFiltro || (
        c.horaComida &&
        new Date(c.horaComida).toDateString() === this.fechaFiltro.toDateString()
      ))
    );
  }

  limpiarFiltros(): void {
    this.ninoFiltro = null;
    this.educadorFiltro = null;
    this.fechaFiltro = null;
  }
  
  editarComida(comida: Comida): void {
    this.editar.emit(comida);
  }

  confirmarEliminacion(comida: Comida): void {
    this.confirmationService.confirm({
      message: '¿Estás seguro de que deseas eliminar esta comida?',
      acceptLabel: 'Sí',
      rejectLabel: 'No',
      accept: () => this.eliminarComida(comida.id!)
    });
  }

  eliminarComida(id: number): void {
    this.comidaService.delete(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Comida eliminada correctamente' });
        this.eliminado.emit();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error al eliminar la comida' });
      }
    });
  }
}
