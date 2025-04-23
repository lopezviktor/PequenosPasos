import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Parent } from '@models/parent.model';
import { ConfirmationService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ParentService } from '@services/parent/parent.service';

@Component({
  selector: 'app-parent-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ConfirmDialogModule],
  templateUrl: './parent-table.component.html',
  styleUrls: ['./parent-table.component.scss']
})
export class ParentTableComponent {
  @Input() parents: Parent[] = [];
  @Input() globalFilter: string = '';
  @Output() editar = new EventEmitter<Parent>();
  @Output() eliminado = new EventEmitter<number>();

  constructor(
    private confirmationService: ConfirmationService,
    private parentService: ParentService
  ) {}

  editarPadre(padre: Parent) {
    this.editar.emit(padre);
  }

  confirmarEliminar(parent: Parent) {
    this.confirmationService.confirm({
      message: `¿Seguro que deseas eliminar a ${parent.nombre}?`,
      header: 'Confirmar eliminación',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.parentService.deleteParent(parent.id).subscribe(() => {
          this.eliminado.emit(parent.id); // Emitimos el id del padre eliminado
        });
      }
    });
  }
}