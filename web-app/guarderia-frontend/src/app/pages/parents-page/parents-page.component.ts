import { Component } from '@angular/core';
import { ParentTableComponent } from '@components/tables/parent-table/parent-table.component';
import { Parent } from '@models/parent.model';

@Component({
  selector: 'app-parents-page',
  imports: [
    ParentTableComponent
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
}
