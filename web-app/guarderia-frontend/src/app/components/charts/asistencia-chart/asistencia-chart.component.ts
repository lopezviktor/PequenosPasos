import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import Chart from 'chart.js/auto';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-asistencia-chart',
  templateUrl: './asistencia-chart.component.html',
  styleUrls: ['./asistencia-chart.component.scss'],
})
export class AsistenciaChartComponent implements OnInit, AfterViewInit {
  @ViewChild('asistenciaCanvas') asistenciaCanvas!: ElementRef<HTMLCanvasElement>;
  chart!: Chart;
  labels: string[] = [];
  data: number[] = [];

  constructor(private asistenciaService: AsistenciaService, private translate: TranslateService) {}

  ngOnInit(): void {
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0 (D) - 6 (S)
    const monday = new Date(today);
    const sunday = new Date(today);
    monday.setDate(today.getDate() - ((dayOfWeek + 6) % 7)); // lunes
    sunday.setDate(monday.getDate() + 6); // domingo

    const inicioStr = this.formatDateTime(monday, '00:00:00');
    const finStr = this.formatDateTime(sunday, '23:59:59');

    this.asistenciaService.getAsistenciasPorRango(inicioStr, finStr).subscribe(res => {
      const conteoPorDia: { [fecha: string]: Set<number> } = {};

      res.forEach(a => {
        const fecha = a.horaEntrada.split('T')[0]; // '2025-05-13'
        if (!conteoPorDia[fecha]) {
          conteoPorDia[fecha] = new Set<number>();
        }
        conteoPorDia[fecha].add(a.nino.id);
      });

      const labels: string[] = [];
      const data: number[] = [];

      for (let i = 0; i < 7; i++) {
        const fecha = new Date(monday);
        fecha.setDate(monday.getDate() + i);
        const fechaStr = fecha.toISOString().split('T')[0];
        const label = fecha.toLocaleDateString('es-ES', { weekday: 'short', day: 'numeric' });

        labels.push(label);
        data.push(conteoPorDia[fechaStr]?.size || 0);
      }

      this.labels = labels;
      this.data = data;

      console.log('Gráfica Asistencia - Labels:', this.labels, 'Data:', this.data);
      this.renderChart(this.labels, this.data);
    });
  }

  ngAfterViewInit(): void {
    this.translate.onLangChange.subscribe(() => {
      this.renderChart(this.labels, this.data);
    });
  }

  private formatDateTime(date: Date, time: string): string {
    const yyyy = date.getFullYear();
    const mm = (date.getMonth() + 1).toString().padStart(2, '0');
    const dd = date.getDate().toString().padStart(2, '0');
    return `${yyyy}-${mm}-${dd}T${time}`;
  }

  private renderChart(labels: string[], data: number[]): void {
    const ctx = this.asistenciaCanvas.nativeElement.getContext('2d');

    const chartColor = getComputedStyle(document.documentElement)
      .getPropertyValue('--chart-color')
      .trim();

    if (this.chart) {
      this.chart.destroy();
    }

    this.translate.stream('GRAFICA_ASISTENCIA.NINOS_POR_DIA').subscribe(titulo => {
      this.chart = new Chart(ctx!, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: titulo,
            data: data,
            backgroundColor: chartColor,
            borderRadius: 4,
            maxBarThickness: 40,
            barPercentage: 0.6
          }]
        },
        options: {
          responsive: true,
          layout: {
            padding: {
              left: 10,
              right: 10
            }
          },
          plugins: {
            legend: {
              display: true,
              labels: {
                boxWidth: 20,
              }
            },
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1,
                precision: 0
              }
            }
          }
        }
      });
    });
  }
}