import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePickerModule } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ResumenClase } from '@models/resumenClase.model';
import { ClassroomService } from '@services/classroom/classroom.service';
import { ChildService } from '@services/child/child.service';
import { AsistenciaService } from '@services/asistencia/asistencia.service';
import { ComidaService } from '@services/comida/comida.service';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { Clase } from '@models/clase.model';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-calendar-widget',
  standalone: true,
  imports: [
    CommonModule,
    DatePickerModule,
    FormsModule,
    TranslateModule
  ],
  templateUrl: './calendar-widget.component.html',
  styleUrl: './calendar-widget.component.scss'
})
export class CalendarWidgetComponent implements OnInit {
  resumenPorClase: ResumenClase[] = [];

  constructor(
    private claseService: ClassroomService,
    private childService: ChildService,
    private asistenciaService: AsistenciaService,
    private comidaService: ComidaService,
    private siestaService: SiestaService
  ) {}

  ngOnInit(): void {
    Promise.all([
      firstValueFrom(this.claseService.getClases()),
      firstValueFrom(this.childService.getAllChildren()),
      firstValueFrom(this.asistenciaService.getAsistenciasDeHoy()),
      firstValueFrom(this.comidaService.getComidasDeHoy()),
      firstValueFrom(this.siestaService.getSiestasDeHoy())
    ]).then(([
      clases,
      ninos,
      asistencias,
      comidas,
      siestas
    ]: [
      Clase[],
      any[],
      any[],
      any[],
      any[]
    ]) => {
      console.log('Clases:', clases);
      console.log('Niños:', ninos);
      console.log('Asistencias:', asistencias);
      console.log('Comidas:', comidas);
      console.log('Siestas:', siestas);
      this.resumenPorClase = clases.map((clase: Clase) => {
        const ninosClase = ninos.filter(n => n.clase?.id === clase.id);
        const presentes = asistencias.filter(a => a.nino && ninosClase.some(n => n.id === a.nino.id)).length;
        const comiendo = comidas.filter(c => c.nino && ninosClase.some(n => n.id === c.nino.id)).length;
        const enSiesta = siestas.filter(s =>
          !!s.nino &&
          s.nino.id !== undefined &&
          !s.finSiesta &&
          ninosClase.some(n => n.id === s.nino!.id)
        ).length;
        
        const nombreClase = clase.nombre.startsWith('Clase ') ? clase.nombre.replace('Clase ', '') : clase.nombre;

        return {
          nombre: nombreClase,
          presentes,
          comiendo,
          enSiesta
        };
      });
    }).catch(error => {
      console.error('Error cargando resumen por clase:', error);
    });
  }
}