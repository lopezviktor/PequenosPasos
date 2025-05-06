import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { HigieneFormComponent } from '@components/forms/higiene-form/higiene-form.component';
import { HigieneTableComponent } from '@components/tables/higiene-table/higiene-table.component';
import { Higiene } from '@models/higiene.model';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-higienes-page',
  standalone: true,
  imports: [CommonModule, DialogModule, HigieneFormComponent, HigieneTableComponent, ButtonModule],
  templateUrl: './higienes-page.component.html',
  styleUrl: './higienes-page.component.scss'
})
export class HigienesPageComponent {
  mostrarDialogo = false;
  higieneSeleccionada?: Higiene;

  @ViewChild(HigieneTableComponent) tabla!: HigieneTableComponent;

  crearHigiene() {
    this.higieneSeleccionada = undefined;
    this.mostrarDialogo = true;
  }

  editarHigiene(higiene: Higiene) {
    this.higieneSeleccionada = higiene;
    this.mostrarDialogo = true;
  }

  cerrarFormulario() {
    this.mostrarDialogo = false;
  }

  recargarTabla() {
    this.tabla.loadHigienes();
  }
}
