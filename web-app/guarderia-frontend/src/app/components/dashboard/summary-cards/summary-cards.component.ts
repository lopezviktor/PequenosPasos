import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { OnInit } from '@angular/core';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { ChildService } from '@services/child/child.service';
import { ComidaService } from '@services/comida/comida.service';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { ActividadService } from '@services/actividad/actividad.service';

@Component({
  selector: 'app-summary-cards',
  imports: [
    CommonModule,
    CardModule
  ],
  templateUrl: './summary-cards.component.html',
  styleUrl: './summary-cards.component.scss'
})

export class SummaryCardsComponent implements OnInit {
  ninosPresentesHoy: number = 0;
  ausenciasHoy: number = 0;
  comidasHoy: number = 0;
  siestasActivas: number = 0;
  ultimaActividadNombre: string = 'Sin actividades';
  
  constructor(
    private asistenciaService: AsistenciaService,
    private childService: ChildService,
    private comidaService: ComidaService,
    private siestaService: SiestaService,
    private actividadService: ActividadService
  ) {}

  ngOnInit(): void {
    this.childService.getAllChildren().subscribe({
      next: (children) => {
        this.asistenciaService.getAsistenciasDeHoy().subscribe({
          next: (asistencias) => {
            this.ninosPresentesHoy = asistencias.length;
            this.ausenciasHoy = children.length - asistencias.length;

            this.comidaService.getComidasDeHoy().subscribe({
              next: (comidas) => {
                this.comidasHoy = comidas.length;
                this.siestaService.getSiestasDeHoy().subscribe({
                  next: (siestas) => {
                    console.log('Siestas de hoy:', siestas);
                    this.siestasActivas = siestas.filter(s => !s.finSiesta).length;
                    this.actividadService.getActividades().subscribe({
                      next: (actividades) => {
                        if (actividades.length > 0) {
                          this.ultimaActividadNombre = actividades[actividades.length - 1].nombre;
                        }
                      },
                      error: (err) => {
                        console.error('Error al obtener actividades:', err);
                      }
                    });
                  },
                  error: (err) => {
                    console.error('Error al obtener siestas de hoy:', err);
                  }
                });
              },
              error: (err) => {
                console.error('Error al obtener comidas de hoy:', err);
              }
            });
          },
          error: (err) => {
            console.error('Error al obtener asistencias de hoy:', err);
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener niños:', err);
      }
    });
  }
}