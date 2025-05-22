import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { KeyFilterModule } from 'primeng/keyfilter';
import { Child } from '@models/child.model';
import { ChildService } from '@services/child/child.service';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-child-table',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    TableModule, 
    ButtonModule, 
    ConfirmDialogModule, 
    KeyFilterModule, 
    DropdownModule, 
    CalendarModule
  ],
  templateUrl: './child-table.component.html',
  styleUrls: ['./child-table.component.scss']
})
export class ChildTableComponent {
  private _children: Child[] = [];
  @Input() set children(value: Child[]) {
    this._children = value;
    this.clasesDisponibles = [...new Set(value.map(c => c.clase?.nombre).filter((nombre): nombre is string => nombre !== undefined))];
  }
  get children(): Child[] {
    return this._children;
  }
  clasesDisponibles: string[] = [];
  @Output() editar = new EventEmitter<Child>();
  @Output() eliminado = new EventEmitter<number>();

  nombreFiltro: string = '';
  apellidosFiltro: string = '';
  claseFiltro: string = '';
  alergiasFiltro: string = '';
  condicionesFiltro: string = '';
  fechaNacimientoDesde: Date | null = null;
  fechaNacimientoHasta: Date | null = null;

  primerDiaDesde: Date | null = null;
  primerDiaHasta: Date | null = null;

  constructor(
    private confirmationService: ConfirmationService,
    private childService: ChildService
  ) {}

  editarNino(nino: Child) {
    this.editar.emit(nino);
  }

  confirmarEliminar(nino: Child) {
    if (nino.id !== undefined) {
      this.confirmationService.confirm({
        message: `¿Seguro que deseas eliminar a ${nino.nombre}?`,
        header: 'Confirmar eliminación',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          this.childService.deleteChild(nino.id!).subscribe(() => {
            this.eliminado.emit(nino.id);
          });
        }
      });
    } else {
      console.error('ID del niño no definido, no se puede eliminar.');
    }
  }

  get childrenFiltrados(): Child[] {
    return this.children.filter(c =>
      c.nombre.toLowerCase().includes(this.nombreFiltro.toLowerCase()) &&
      c.apellidos.toLowerCase().includes(this.apellidosFiltro.toLowerCase()) &&
      (c.clase?.nombre ?? '').toLowerCase().includes((this.claseFiltro ?? '').toLowerCase()) &&
      (c.alergias?.toLowerCase().includes(this.alergiasFiltro.toLowerCase()) ?? false) &&
      (this.condicionesFiltro === '' || c.condicionesMedicas?.toLowerCase().includes(this.condicionesFiltro.toLowerCase())) &&
      (!this.fechaNacimientoDesde || new Date(c.fechaNacimiento) >= this.fechaNacimientoDesde) &&
      (!this.fechaNacimientoHasta || new Date(c.fechaNacimiento) <= this.fechaNacimientoHasta) &&
      (!this.primerDiaDesde || new Date(c.primerDia) >= this.primerDiaDesde) &&
      (!this.primerDiaHasta || new Date(c.primerDia) <= this.primerDiaHasta)
    );
  }

  limpiarFiltros() {
    this.nombreFiltro = '';
    this.apellidosFiltro = '';
    this.claseFiltro = '';
    this.alergiasFiltro = '';
    this.condicionesFiltro = '';
    this.fechaNacimientoDesde = null;
    this.fechaNacimientoHasta = null;
    this.primerDiaDesde = null;
    this.primerDiaHasta = null;
  }
}
