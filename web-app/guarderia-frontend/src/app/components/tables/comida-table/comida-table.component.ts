import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comida } from '@models/comida.model';
import { ComidaService } from '@services/comida/comida.service';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-comida-table',
  imports: [
    CommonModule,
    TableModule,
    ButtonModule
  ],
  templateUrl: './comida-table.component.html',
  styleUrl: './comida-table.component.scss'
})
export class ComidaTableComponent {

  @Input() comidas: Comida[] = [];
  @Output() editar = new EventEmitter<Comida>();
  @Output() eliminar = new EventEmitter<number>();

  constructor(private comidaService: ComidaService) {}
  
  editarComida(comida: Comida): void {
    this.editar.emit(comida);  // Emitir el objeto comida hacia el componente padre
  }

  eliminarComida(id: number): void {
    this.eliminar.emit(id); // Emitir el id hacia el componente padre
  }

}
