import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { Siesta } from '@models/siesta.model';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-siesta-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ConfirmDialogModule],
  templateUrl: './siesta-table.component.html',
  styleUrl: './siesta-table.component.scss',
  providers: [MessageService, ConfirmationService]
})
export class SiestaTableComponent {
  @Input() siestas: Siesta[] = [];
  @Output() editar = new EventEmitter<Siesta>();
  @Output() eliminado = new EventEmitter<void>();

  constructor(
    private siestaService: SiestaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

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
