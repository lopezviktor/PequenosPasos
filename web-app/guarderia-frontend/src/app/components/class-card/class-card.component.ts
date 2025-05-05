import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Clase } from '@models/clase.model';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-class-card',
  imports: [CardModule],
  templateUrl: './class-card.component.html',
  styleUrl: './class-card.component.scss'
})
export class ClassCardComponent {
  @Input() clase!: Clase;
  @Output() editar = new EventEmitter<Clase>();
  @Output() eliminar = new EventEmitter<Clase>();

  onEditar() {
    this.editar.emit(this.clase);
  }

  onEliminar() {
    this.eliminar.emit(this.clase);
  }
  
}
