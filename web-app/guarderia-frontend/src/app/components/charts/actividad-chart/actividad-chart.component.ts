import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { ActividadService } from '@services/actividad/actividad.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-actividad-chart',
  standalone: true,
  templateUrl: './actividad-chart.component.html',
  styleUrl: './actividad-chart.component.scss'
})
export class ActividadChartComponent implements AfterViewInit {
  @ViewChild('actividadCanvas') actividadCanvas!: ElementRef<HTMLCanvasElement>;
  chart: Chart | undefined;
  private labels: string[] = [];
  private data: number[] = [];

  constructor(private actividadService: ActividadService, private translate: TranslateService) {}

  ngAfterViewInit(): void {
    this.actividadService.getActividadNinos().subscribe(registros => {
      const conteoPorClase = new Map<string, number>();

      registros.forEach((registro: any) => {
        const claseNombre = registro.nino?.clase?.nombre;
        if (claseNombre) {
          conteoPorClase.set(claseNombre, (conteoPorClase.get(claseNombre) || 0) + 1);
        }
      });

      this.labels = Array.from(conteoPorClase.keys());
      this.data = Array.from(conteoPorClase.values());
      this.renderChart(this.labels, this.data);

      this.translate.onLangChange.subscribe(() => {
        this.renderChart(this.labels, this.data);
      });
    });
  }

  private renderChart(labels: string[], data: number[]): void {
    if (this.chart) {
      this.chart.destroy();
    }
    const ctx = this.actividadCanvas.nativeElement.getContext('2d');
    const chartColor = getComputedStyle(this.actividadCanvas.nativeElement.parentElement!)
      .getPropertyValue('--chart-color')
      .trim();

    this.translate.stream('GRAFICA_ACTIVIDAD.TITULO').subscribe(titulo => {
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
    });
  }
}
