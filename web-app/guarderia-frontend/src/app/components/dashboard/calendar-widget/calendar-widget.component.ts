import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePickerModule } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';

interface Evento {
  hora: string;
  descripcion: string;
  tipo: 'pañal' | 'comida' | 'siesta' | 'actividad';
  icono: string;
}

@Component({
  selector: 'app-calendar-widget',
  standalone: true,
  imports: [
    CommonModule,
    DatePickerModule,
    FormsModule
  ],
  templateUrl: './calendar-widget.component.html',
  styleUrl: './calendar-widget.component.scss'
})
export class CalendarWidgetComponent {
  selectedDate: Date = new Date();

  eventos: Evento[] = [
    { hora: '10:30', descripcion: 'Higiene de Mario', tipo: 'pañal', icono: '🧼' },
    { hora: '12:15', descripcion: 'Comida de Julia', tipo: 'comida', icono: '🍽️' },
    { hora: '15:00', descripcion: 'Actividad: Pintura', tipo: 'actividad', icono: '✨' }
  ];

  get eventosDelDia(): Evento[] {
    // En el futuro aquí puedes filtrar por fecha
    return this.eventos;
  }
}