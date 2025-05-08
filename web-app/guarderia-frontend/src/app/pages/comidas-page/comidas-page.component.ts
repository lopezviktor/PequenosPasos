import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comida } from '@models/comida.model';
import { ComidaService } from '@services/comida/comida.service';
import { ComidaFormComponent } from '@components/forms/comida-form/comida-form.component';
import { ComidaTableComponent } from '@components/tables/comida-table/comida-table.component';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-comidas-page',
  standalone: true,
  imports: [
    CommonModule,
    ComidaFormComponent,
    ComidaTableComponent,
    DialogModule,
    ButtonModule
  ],
  templateUrl: './comidas-page.component.html',
  styleUrls: ['./comidas-page.component.scss']
})
export class ComidasPageComponent {
  comidas: Comida[] = [];
  comidaSeleccionada?: Comida;
  mostrarDialogo = false;

  constructor(private comidaService: ComidaService) {}

  ngOnInit(): void {
    this.cargarComidas();
  }

  cargarComidas(): void {
    this.comidaService.getAll().subscribe(comidas => {
      this.comidas = comidas;
    });
  }

  crearComida(): void {
    this.comidaSeleccionada = undefined;
    this.mostrarDialogo = true;
  }

  editarComida(comida: Comida): void {
    this.comidaSeleccionada = comida;
    this.mostrarDialogo = true;
  }

  cerrarDialogo(): void {
    this.mostrarDialogo = false;
  }

  guardarComida(): void {
    this.cargarComidas();
  }
}