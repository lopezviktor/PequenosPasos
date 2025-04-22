import { Component } from '@angular/core';
import { ParentTableComponent } from '@components/tables/parent-table/parent-table.component';
import { ParentFormComponent } from '@components/forms/parent-form/parent-form.component';
import { DialogModule } from 'primeng/dialog';
import { Parent } from '@models/parent.model';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-parents-page',
  standalone: true,
  imports: [
    CommonModule,
    ParentTableComponent,
    ParentFormComponent,
    DialogModule,
    ButtonModule
  ],
  templateUrl: './parents-page.component.html',
  styleUrl: './parents-page.component.scss'
})
export class ParentsPageComponent {
  globalFilter: string = '';

  parents: Parent[] = [
    {
      id: 1,
      nombre: 'Víctor',
      apellidos: 'López',
      email: 'victorlopez@pruebas.com',
      telefono: '123456789',
    },
    {
      id: 2,
      nombre: 'Laura',
      apellidos: 'Martínez',
      email: 'laura@correo.com',
      telefono: '987654321',
    }
  ];
  
  mostrarDialogoPadre = false;

  guardarPadre(padre: Parent) {
    console.log('Guardar:', padre);
    this.mostrarDialogoPadre = false;
  }

  editarPadre(parent: Parent) {
    console.log('Editar padre:', parent);
  }
}
