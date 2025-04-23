import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { Educator } from '@models/educator.model';
import { EducatorService } from '@services/educator/educator.service';

@Component({
  selector: 'app-educator-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ConfirmDialogModule],
  templateUrl: './educator-table.component.html',
  styleUrls: ['./educator-table.component.scss']
})
export class EducatorTableComponent {
  @Input() educators: Educator[] = [];
  @Input() globalFilter: string = '';
  @Output() editar = new EventEmitter<Educator>();
  @Output() eliminado = new EventEmitter<number>();

  constructor(
    private confirmationService: ConfirmationService,
    private educatorService: EducatorService
  ) {}

  editarEducador(educador: Educator) {
    this.editar.emit(educador);
  }

  confirmarEliminar(educador: Educator) {
    this.confirmationService.confirm({
      message: `¿Seguro que deseas eliminar a ${educador.nombre}?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.educatorService.deleteEducator(educador.id).subscribe(() => {
          this.eliminado.emit(educador.id);
        });
      }
    });
  }
}
