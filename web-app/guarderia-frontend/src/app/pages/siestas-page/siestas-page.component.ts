import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Siesta } from '@models/siesta.model';
import { SiestaService } from '@services/siesta/siesta-service.service';
import { SiestaFormComponent } from '@components/forms/siesta-form/siesta-form.component';
import { SiestaTableComponent } from '@components/tables/siesta-table/siesta-table.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-siestas-page',
  standalone: true,
  imports: [
    CommonModule,
    SiestaFormComponent,
    SiestaTableComponent,
    DialogModule,
    ButtonModule
  ],
  templateUrl: './siestas-page.component.html',
  styleUrl: './siestas-page.component.scss'
})
export class SiestasPageComponent {
  siestas: Siesta[] = [];
  siestaSeleccionada?: Siesta;
  mostrarDialogo = false;

  constructor(private siestaService: SiestaService) {}

  ngOnInit(): void {
    this.cargarSiestas();
  }

  cargarSiestas(): void {
    this.siestaService.getSiestas().subscribe(siestas => {
      this.siestas = siestas;
    });
  }

  crearSiesta(): void {
    this.siestaSeleccionada = undefined;
    this.mostrarDialogo = true;
  }

  editarSiesta(siesta: Siesta): void {
    this.siestaSeleccionada = siesta;
    this.mostrarDialogo = true;
  }

  cerrarDialogo(): void {
    this.mostrarDialogo = false;
  }

  guardarSiesta(): void {
    this.cargarSiestas();
  }
}
