import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { OnInit } from '@angular/core';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { ChildService } from '@services/child/child.service';
import { ComidaService } from '@services/comida/comida.service';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { ActividadService } from '@services/actividad/actividad.service';
import { NotificacionesService } from '@services/notificaciones/notificaciones.service';
import { AuthService } from '@services/auth/auth.service';
import { HigieneService } from '@services/higiene/higiene.service';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-summary-cards',
  imports: [
    CommonModule,
    CardModule,
    TranslateModule
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
  notificacionesNoLeidas: number = 0;
  totalNinos: number = 0;
  ninosSinActividadHoy: number = 0;
  promedioActividadesPorNino: number = 0;
  higienesHoy: number = 0;
  constructor(
    private asistenciaService: AsistenciaService,
    private childService: ChildService,
    private comidaService: ComidaService,
    private siestaService: SiestaService,
    private actividadService: ActividadService,
    private notificacionService: NotificacionesService,
    private authService: AuthService,
    private higieneService: HigieneService,
  ) {}

  ngOnInit(): void {
    this.childService.getAllChildren().subscribe({
      next: (children) => {
        this.asistenciaService.getAsistenciasDeHoy().subscribe({
          next: (asistencias) => {
            this.ninosPresentesHoy = asistencias.length;
            this.ausenciasHoy = children.length - asistencias.length;
            this.totalNinos = children.length;
            this.actividadService.getActividadNinos().subscribe({
              next: (actividadNinos) => {
                const hoy = new Date();
                const actividadHoy = actividadNinos.filter(a => {
                  const fecha = new Date(a.fechaRegistro);
                  return fecha.toDateString() === hoy.toDateString();
                });

                const ninosConActividadHoy = new Set(actividadHoy.map(a => a.nino.id));
                this.ninosSinActividadHoy = children.filter(n => !ninosConActividadHoy.has(n.id)).length;
                this.promedioActividadesPorNino = actividadHoy.length / children.length;
              },
              error: (err) => {
                console.error('Error al obtener actividad-ninos:', err);
              }
            });
            this.comidaService.getComidasDeHoy().subscribe({
              next: (comidas) => {
                this.comidasHoy = comidas.length;
                this.siestaService.getSiestasDeHoy().subscribe({
                  next: (siestas) => {
                    this.siestasActivas = siestas.filter(s => !s.finSiesta).length;
                    this.actividadService.getActividades().subscribe({
                      next: (actividades) => {
                        if (actividades.length > 0) {
                          this.ultimaActividadNombre = actividades[actividades.length - 1].nombre;
                        }
                        const userId = this.authService.getUserIdFromToken();
                        if (userId) {
                          this.notificacionService.getNoLeidasPorUsuario(userId).subscribe({
                            next: (notificaciones) => {
                              this.notificacionesNoLeidas = notificaciones.length;
                              this.higieneService.getHigienesDeHoy().subscribe({
                                next: (higienes) => {
                                  this.higienesHoy = higienes.length;
                                },
                                
                                error: (err) => {
                                  console.error('Error al obtener registros de higiene de hoy:', err);
                                }
                              });
                            },
                            error: (err) => {
                              console.error('Error al obtener notificaciones no leídas:', err);
                            }
                          });
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