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
      routerLink: ['/dashboard']
    },
    {
      label: 'Educadores',
      icon: 'pi pi-user-edit',
      routerLink: ['/educadores']
    },
    {
      label: 'Niños',
      icon: 'pi pi-users',
      routerLink: ['/ninos']
    },
    {
      label: 'Clases',
      icon: 'pi pi-users',
      routerLink: ['/clases']
    },
    {
      label: 'Padres',
      icon: 'pi pi-users',
      routerLink: ['/padres']
    },
    {
      label: 'Asistencia',
      icon: 'pi pi-calendar-check',
      routerLink: ['/asistencias']
    },
    {
      label: 'Comidas',
      icon: 'pi pi-utensils',
      routerLink: ['/comidas']
    },
    {
      label: 'Higienes',
      icon: 'pi pi-shield',
      routerLink: ['/higienes']
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
    }
  ]
}
