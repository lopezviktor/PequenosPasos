import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { OnInit } from '@angular/core';
import { AsistenciaService } from '@services/asistencia/asistencia.service';

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

  constructor(private asistenciaService: AsistenciaService) {}

  ngOnInit(): void {
    this.asistenciaService.getAsistenciasDeHoy().subscribe({
      next: (asistencias) => {
        console.log('Asistencias de hoy:', asistencias);
        this.ninosPresentesHoy = asistencias.length;
      },
      error: (err) => {
        console.error('Error al obtener asistencias de hoy:', err);
      }
    });
  }
}