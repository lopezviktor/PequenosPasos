import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelMenuModule } from 'primeng/panelmenu';
import { RouterModule, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-sidebar',
  imports: [
    CommonModule,
    PanelMenuModule,
    RouterModule,
    ButtonModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  constructor(private router: Router) {}

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
      icon: 'pi pi-star',
      routerLink: ['/ninos']
    },
    {
      label: 'Clases',
      icon: 'pi pi-users',
      routerLink: ['/clases']
    },
    {
      label: 'Padres',
      icon: 'pi pi-plus',
      routerLink: ['/padres']
    },
    {
      label: 'Asistencia',
      icon: 'pi pi-clock',
      routerLink: ['/asistencias']
    },
    {
      label: 'Comidas',
      icon: 'pi pi-apple',
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

  irAlPerfil() {
    this.router.navigate(['/perfil']);
  }

  cerrarSesion() {
    // Aquí podrías limpiar el token, llamar a AuthService, etc.
    this.router.navigate(['/login']);
  }
}
