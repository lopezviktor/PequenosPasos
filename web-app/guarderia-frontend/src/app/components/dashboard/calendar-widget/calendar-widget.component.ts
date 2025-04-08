import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-calendar-widget',
  imports: [
    CommonModule,
    CalendarModule,
    FormsModule
  ],
  templateUrl: './calendar-widget.component.html',
  styleUrl: './calendar-widget.component.scss'
})
export class CalendarWidgetComponent {
  selectedDate: Date = new Date();
}
