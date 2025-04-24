import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { Child } from '@models/child.model';
import { ChildService } from '@services/child/child.service';

@Component({
  selector: 'app-child-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ConfirmDialogModule],
  templateUrl: './child-table.component.html',
  styleUrls: ['./child-table.component.scss']
})
export class ChildTableComponent {
  @Input() children: Child[] = [];
  @Input() globalFilter: string = '';
  @Output() editar = new EventEmitter<Child>();
  @Output() eliminado = new EventEmitter<number>();

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
}
