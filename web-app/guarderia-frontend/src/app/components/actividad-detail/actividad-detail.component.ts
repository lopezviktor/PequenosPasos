import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ActividadService } from '@services/actividad/actividad.service';
import { ActividadConDetalles } from '@models/actividad.model';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ListboxModule } from 'primeng/listbox';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-actividad-detail',
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    ListboxModule,
    TranslateModule
  ],
  templateUrl: './actividad-detail.component.html',
  styleUrl: './actividad-detail.component.scss'
})
export class ActividadDetailComponent implements OnInit {
  actividad: ActividadConDetalles = {
    actividadId: 0,
    nombre: '',
    descripcion: '',
    claseNombre: '',
    educadorNombre: '',
    ninos: []
  };

  constructor(
    private route: ActivatedRoute,
    private actividadService: ActividadService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    forkJoin([
      this.actividadService.getActividad(id),
      this.actividadService.getActividadDetalles(id)
    ]).subscribe({
      next: ([actividad, detalles]) => {
        const detallesArray = Array.isArray(detalles) ? detalles : [];
        
        this.actividad = {
          actividadId: actividad.actividadId,
          nombre: actividad.nombre,
          descripcion: actividad.descripcion,
          claseNombre: detallesArray[0]?.nino?.clase?.nombre || 'Sin clase',
          educadorNombre: `${detallesArray[0]?.nino?.clase?.educador?.nombre || ''} ${detallesArray[0]?.nino?.clase?.educador?.apellidos || ''}`,
          ninos: detallesArray.map((detalle: any) => ({
            id: detalle.nino.id,
            nombre: detalle.nino.nombre,
            apellidos: detalle.nino.apellidos,
            fechaNacimiento: detalle.nino.fechaNacimiento || '',
            primerDia: detalle.nino.primerDia || '',
            alergias: detalle.nino.alergias || '',
            condicionesMedicas: detalle.nino.condicionesMedicas || '',
            fotoUrl: detalle.nino.fotoUrl || ''
          }))
        };
        console.log('Actividad cargada:', this.actividad);
      },
      error: (err) => console.error('Error al cargar la actividad:', err)
    });
  }

  get ninosOptions(): { label: string, value: number }[] {
    return this.actividad.ninos?.map(nino => ({ label: `${nino.nombre} ${nino.apellidos}`, value: nino.id || 0 })) || [];
  }

  volver(): void {
    window.history.back();
  }
}
