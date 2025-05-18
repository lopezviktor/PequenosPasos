import { Component, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SummaryCardsComponent } from '../../components/dashboard/summary-cards/summary-cards.component';
import { CalendarWidgetComponent } from '../../components/dashboard/calendar-widget/calendar-widget.component';
import Chart from 'chart.js/auto';
import { AsistenciaChartComponent } from '@components/charts/asistencia-chart/asistencia-chart.component';
import { EntradaChartComponent } from '@components/charts/entrada-chart/entrada-chart.component';

@Component({
  selector: 'app-dashboard-page',
  imports: [
    CommonModule,
    SummaryCardsComponent,
    CalendarWidgetComponent,
    AsistenciaChartComponent,
    EntradaChartComponent
  ],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent implements AfterViewInit {
  ngAfterViewInit(): void {
    this.renderRankingChart();
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
