import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { ActividadService } from '@services/actividad/actividad.service';

@Component({
  selector: 'app-actividad-chart',
  standalone: true,
  templateUrl: './actividad-chart.component.html',
  styleUrl: './actividad-chart.component.scss'
})
export class ActividadChartComponent implements AfterViewInit {
  @ViewChild('actividadCanvas') actividadCanvas!: ElementRef<HTMLCanvasElement>;
  chart: Chart | undefined;

  constructor(private actividadService: ActividadService) {}

  ngAfterViewInit(): void {
    this.actividadService.getActividadNinos().subscribe(registros => {
      const conteoPorClase = new Map<string, number>();

      registros.forEach((registro: any) => {
        const claseNombre = registro.nino?.clase?.nombre;
        if (claseNombre) {
          conteoPorClase.set(claseNombre, (conteoPorClase.get(claseNombre) || 0) + 1);
        }
      });

      const labels = Array.from(conteoPorClase.keys());
      const data = Array.from(conteoPorClase.values());

      this.renderChart(labels, data);
    });
  }

  private renderChart(labels: string[], data: number[]): void {
    const ctx = this.actividadCanvas.nativeElement.getContext('2d');
    const chartColor = getComputedStyle(this.actividadCanvas.nativeElement.parentElement!)
      .getPropertyValue('--chart-color')
      .trim();

    this.chart = new Chart(ctx!, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Actividades por clase',
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
            display: true
          }
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
  }
}
