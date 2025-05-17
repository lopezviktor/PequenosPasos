import { Component, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../../components/dashboard/summary-cards/summary-cards.component';
import { CalendarWidgetComponent } from '../../components/dashboard/calendar-widget/calendar-widget.component';
import Chart from 'chart.js/auto';


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
export class DashboardPageComponent implements AfterViewInit {
  ngAfterViewInit(): void {
    this.renderAttendanceChart();
    this.renderEntryChart();
    this.renderRankingChart();
  }

  renderAttendanceChart() {
    new Chart('attendanceChart', {
      type: 'bar',
      data: {
        labels: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie'],
        datasets: [{
          label: 'Niños presentes',
          data: [12, 10, 9, 11, 13],
          backgroundColor: '#8BC34A'
        }]
      }
    });
  }

  renderEntryChart() {
    new Chart('entryChart', {
      type: 'line',
      data: {
        labels: ['08:00', '09:00', '10:00', '11:00', '12:00'],
        datasets: [{
          label: 'Entradas por hora',
          data: [2, 8, 5, 1, 0],
          borderColor: '#FDD835',
          backgroundColor: 'rgba(253, 216, 53, 0.2)',
          fill: true,
          tension: 0.4
        }]
      }
    });
  }

  renderRankingChart() {
    new Chart('rankingChart', {
      type: 'bar',
      data: {
        labels: ['Pollitos', 'Ardillas', 'Renacuajos'],
        datasets: [{
          label: 'Actividades',
          data: [10, 8, 5],
          backgroundColor: '#FFB74D'
        }]
      }
    });
  }
}
