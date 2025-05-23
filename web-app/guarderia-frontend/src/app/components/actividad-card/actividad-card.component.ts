import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ActividadConDetalles } from '@models/actividad.model';
import { DatePipe } from '@angular/common';

import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-actividad-card',
  standalone: true,
  imports: [CommonModule, ButtonModule, ConfirmDialogModule, TranslateModule],
  templateUrl: './actividad-card.component.html',
  styleUrls: ['./actividad-card.component.scss']
})
export class ActividadCardComponent {

  @Input() actividad!: ActividadConDetalles;
  @Output() editar = new EventEmitter<ActividadConDetalles>();
  @Output() eliminar = new EventEmitter<number>();

  constructor(private router: Router) {}

  onEdit(): void {
    console.log('Editando actividad:', this.actividad);
    this.editar.emit(this.actividad);
  }

  onDelete(): void {
    console.log('Actividad para eliminar:', this.actividad);
    const id = this.actividad.actividadId || this.actividad.id;
    if (id) {
        this.eliminar.emit(id);
        console.log('ID emitido para eliminación:', id);
    } else {
        console.error('ID no válido para eliminar actividad:', this.actividad);
    }
  }

  verDetalles(): void {
    this.router.navigate(['/actividades', this.actividad.id]);
  }
  
}
