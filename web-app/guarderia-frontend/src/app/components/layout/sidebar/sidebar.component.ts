import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelMenuModule } from 'primeng/panelmenu';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [
    CommonModule,
    PanelMenuModule,
    RouterModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  items = [
    {
      label: 'Dashboard',
      icon: 'pi pi-home',
      routerLink: ['/']
    },
    {
      label: 'Niños',
      icon: 'pi pi-users',
      routerLink: ['/ninos']
    },
        {
      label: 'Padres',
      icon: 'pi pi-users',
      routerLink: ['/padres']
    },
    {
      label: 'Asistencia',
      icon: 'pi pi-calendar-check',
      routerLink: ['/asistencia']
    },
    {
      label: 'Comidas',
      icon: 'pi pi-utensils',
      routerLink: ['/comidas']
    },
    {
      label: 'Higiene',
      icon: 'pi pi-shield',
      routerLink: ['/higiene']
    },
    {
      label: 'Siestas',
      icon: 'pi pi-moon',
      routerLink: ['/siestas']
    },
    {
      label: 'Actividades',
      icon: 'pi pi-book',
      routerLink: ['/actividades']
    },
    {
      label: 'Mensajes',
      icon: 'pi pi-comments',
      routerLink: ['/mensajes']
    },
    {
      label: 'Clases',
      icon: 'pi pi-users',
      routerLink: ['/clases']
    },
    {
      label: 'Educadores',
      icon: 'pi pi-user-edit',
      routerLink: ['/educadores']
    }
  ]
}
