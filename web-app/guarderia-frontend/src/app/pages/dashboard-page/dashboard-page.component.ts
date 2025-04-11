import { SidebarComponent } from './../../components/layout/sidebar/sidebar.component';
import { TopbarComponent } from '../../components/layout/topbar/topbar.component';
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../../components/dashboard/summary-cards/summary-cards.component';
import { CalendarWidgetComponent } from '../../components/dashboard/calendar-widget/calendar-widget.component';
import { FloatingActionButtonComponent } from '../../components/layout/floating-action-button/floating-action-button.component';


@Component({
  selector: 'app-dashboard-page',
  imports: [
    CommonModule,
    SidebarComponent,
    TopbarComponent,
    SummaryCardsComponent,
    CalendarWidgetComponent,
    FloatingActionButtonComponent
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {

}
