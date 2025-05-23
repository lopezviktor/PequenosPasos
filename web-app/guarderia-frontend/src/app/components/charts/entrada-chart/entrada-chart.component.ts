import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { Asistencia } from '@models/asistencia.model';
import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-entrada-chart',
  standalone: true,
  templateUrl: './entrada-chart.component.html',
  styleUrl: './entrada-chart.component.scss'
})
export class EntradaChartComponent implements AfterViewInit {
  @ViewChild('entradaCanvas') entradaCanvas!: ElementRef<HTMLCanvasElement>;
  chart: Chart | undefined;
  labels: string[] = [];
  data: number[] = [];

  constructor(private asistenciaService: AsistenciaService, private translate: TranslateService) {}

  ngAfterViewInit(): void {
    this.asistenciaService.getAsistenciasDeHoy().subscribe(asistencias => {
      const horas: string[] = ['08:00', '09:00', '10:00', '11:00', '12:00'];
      const conteoPorHora = new Array(horas.length).fill(0);

      asistencias.forEach(a => {
        const hora = new Date(a.horaEntrada).getHours();
        const indice = horas.findIndex(h => parseInt(h.split(':')[0]) === hora);
        if (indice !== -1) conteoPorHora[indice]++;
      });

      this.labels = horas;
      this.data = conteoPorHora;
      this.renderChart(this.labels, this.data);

      this.translate.onLangChange.subscribe(() => {
        this.renderChart(this.labels, this.data);
      });
    });
  }

  private renderChart(labels: string[], data: number[]): void {
    const ctx = this.entradaCanvas.nativeElement.getContext('2d');
    const chartColor = getComputedStyle(this.entradaCanvas.nativeElement.parentElement!)
      .getPropertyValue('--chart-color')
      .trim();

    if (this.chart) {
      this.chart.destroy();
    }

    this.translate.stream('GRAFICA_ENTRADA.ENTRADAS_POR_HORA').subscribe(titulo => {
      this.chart = new Chart(ctx!, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: titulo,
            data: data,
            fill: true,
            tension: 0.4,
            borderColor: chartColor,
            backgroundColor: chartColor,
            pointBackgroundColor: chartColor,
            pointBorderColor: '#fff',
            pointRadius: 4,
            pointHoverRadius: 6
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
                boxWidth: 20
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
