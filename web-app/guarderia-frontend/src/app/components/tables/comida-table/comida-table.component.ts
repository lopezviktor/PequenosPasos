import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comida } from '@models/comida.model';
import { ComidaService } from '@services/comida/comida.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-comida-table',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    ConfirmDialogModule
  ],
  templateUrl: './comida-table.component.html',
  styleUrl: './comida-table.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class ComidaTableComponent {

  @Input() comidas: Comida[] = [];
  @Output() editar = new EventEmitter<Comida>();
  @Output() eliminado = new EventEmitter<void>();

  constructor(
    private comidaService: ComidaService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}
  
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
