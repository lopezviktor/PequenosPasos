import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Higiene } from '@models/higiene.model';
import { HigieneService } from '@services/higiene/higiene.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-higiene-table',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ConfirmDialogModule],
  templateUrl: './higiene-table.component.html',
  styleUrl: './higiene-table.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class HigieneTableComponent implements OnInit {
  higienes: Higiene[] = [];
  @Output() editar = new EventEmitter<Higiene>();

  constructor(
    private higieneService: HigieneService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadHigienes();
  }

  loadHigienes(): void {
    this.higieneService.getAll().subscribe(h => this.higienes = h);
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
