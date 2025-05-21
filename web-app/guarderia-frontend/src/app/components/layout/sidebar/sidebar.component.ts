import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelMenuModule } from 'primeng/panelmenu';
import { RouterModule, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '@services/auth/auth.service';
import { OnInit } from '@angular/core';

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

export class SidebarComponent implements OnInit {
  public tipoUsuario: string | null;
  items: any[] = [];

  constructor(private router: Router, private authService: AuthService) {
    this.tipoUsuario = this.authService.getTipoUsuarioFromToken();
  }
ngOnInit(): void {
    this.items = [
      {
        label: 'Dashboard',
        icon: 'pi pi-home',
        routerLink: ['/dashboard']
      },
      ...(this.tipoUsuario === 'ADMINISTRADOR' ? [{
        label: 'Educadores',
        icon: 'pi pi-user-edit',
        routerLink: ['/educadores']
      }] : []),
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
    ];
  }

  irAlPerfil() {
    this.router.navigate(['/profile']);
  }

  cerrarSesion() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}