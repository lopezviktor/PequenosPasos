import { Component, OnInit } from '@angular/core';
import { Comida } from '@models/comida.model';
import { ComidaService } from '@services/comida/comida.service';
import { CommonModule } from '@angular/common';
import { ComidaFormComponent } from '@components/forms/comida-form/comida-form.component'; 
import { ComidaTableComponent } from '@components/tables/comida-table/comida-table.component';
import { Dialog } from 'primeng/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-comidas-page',
  standalone: true,
  imports: [
    ReactiveFormsModule, 
    CommonModule, 
    ComidaFormComponent, 
    Dialog, 
    ComidaTableComponent,
    ButtonModule
  ],
  templateUrl: './comidas-page.component.html',
  styleUrls: ['./comidas-page.component.scss']
})
export class ComidasPageComponent implements OnInit {
  comidas: Comida[] = [];
  comidaSeleccionada: Comida | null = null;
  mostrarDialogo: boolean = false;
  loading: boolean = false;

  constructor(private comidaService: ComidaService) {}

  ngOnInit(): void {
    this.cargarComidas();
  }

  cargarComidas(): void {
    this.loading = true;
    this.comidaService.getAll().subscribe({
      next: (comidas) => {
        this.comidas = comidas;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar comidas:', err);
        this.loading = false;
      }
    });
  }

  abrirDialogoParaNuevaComida(): void {
    this.comidaSeleccionada = null; // Si es una nueva comida, no se selecciona ninguna comida
    this.mostrarDialogo = true; // Mostrar el formulario
  }

  editarComida(comida: Comida): void {
    this.comidaSeleccionada = { ...comida }; // Cargar los datos de la comida seleccionada
    this.mostrarDialogo = true; // Mostrar el formulario
  }

  eliminarComida(id: number): void {
    this.comidaService.deleteComida(id).subscribe({
      next: () => this.cargarComidas(),
      error: (err) => console.error('Error al eliminar comida:', err)
    });
  }
  
  guardarComida(comida: Comida): void {
    const obs = comida.id
      ? this.comidaService.updateComida(comida.id, comida) 
      : this.comidaService.createComida(comida);

    obs.subscribe({
      next: () => {
        this.mostrarDialogo = false;
        this.cargarComidas();
        this.cerrarDialogo();
        this.recargarTabla();
      },
      error: (err) => {
        console.error('Error al guardar comida:', err);
        this.cerrarDialogo();
        this.recargarTabla();
      }
    });
  }

  cerrarDialogo(): void {
    this.mostrarDialogo = false;
  }

  recargarTabla() {
    this.comidaService.getAll().subscribe((comidas) => {
      this.comidas = comidas;
    });
  }
}