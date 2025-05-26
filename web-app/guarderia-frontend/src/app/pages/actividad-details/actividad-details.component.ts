import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { ActividadService } from '@services/actividad/actividad.service';
import { ActividadConDetalles } from '@models/actividad.model';
import { TranslateModule } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-actividad-details',
  imports: [
    CommonModule,
    TranslateModule,
    ButtonModule
  ],
  templateUrl: './actividad-details.component.html',
  styleUrl: './actividad-details.component.scss'
})
export class ActividadDetailComponent implements OnInit {

  actividad?: ActividadConDetalles;

  constructor(
    private route: ActivatedRoute,
    private actividadService: ActividadService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.cargarActividad(id);
    }
  }

  cargarActividad(id: number): void {
    this.actividadService.getActividadDetalles(id).subscribe(
      (actividad) => {
        this.actividad = actividad;
        console.log('Detalles de la actividad:', this.actividad);
      },
      (error) => {
        console.error('Error al cargar los detalles de la actividad:', error);
      }
    );
  }

  volver(): void {
    this.router.navigate(['/actividades']);
  }
}

