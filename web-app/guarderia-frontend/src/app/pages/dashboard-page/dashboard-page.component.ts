import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../../components/dashboard/summary-cards/summary-cards.component';
import { CalendarWidgetComponent } from '../../components/dashboard/calendar-widget/calendar-widget.component';


@Component({
  selector: 'app-dashboard-page',
  imports: [
    CommonModule,
    SummaryCardsComponent,
    CalendarWidgetComponent
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {

}
