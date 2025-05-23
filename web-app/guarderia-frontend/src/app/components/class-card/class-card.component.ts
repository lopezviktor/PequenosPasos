import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Clase } from '@models/clase.model';
import { CardModule } from 'primeng/card';

import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TranslateModule } from '@ngx-translate/core';
@Component({
  selector: 'app-class-card',
  standalone: true,
  imports: [CardModule, ButtonModule, ConfirmDialogModule, TranslateModule],
  templateUrl: './class-card.component.html',
  styleUrl: './class-card.component.scss'
})
export class ClassCardComponent {
  @Input() clase!: Clase;
  @Output() editar = new EventEmitter<Clase>();
  @Output() eliminar = new EventEmitter<Clase>();
  @Output() verDetalles = new EventEmitter<Clase>();

  onEditar() {
    this.editar.emit(this.clase);
  }

  onEliminar() {
    this.eliminar.emit(this.clase);
  }

  onVerDetalles() {
    this.verDetalles.emit(this.clase);
  }
  
}
