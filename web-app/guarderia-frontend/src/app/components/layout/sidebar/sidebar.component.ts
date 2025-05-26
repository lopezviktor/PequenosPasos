import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PanelMenuModule } from 'primeng/panelmenu';
import { RouterModule, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { AuthService } from '@services/auth/auth.service';
import { OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { TranslateModule } from '@ngx-translate/core';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-sidebar',
  imports: [
    CommonModule,
    PanelMenuModule,
    RouterModule,
    ButtonModule,
    TranslateModule,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})

export class SidebarComponent implements OnInit {
  public tipoUsuario: string | null;
  items: any[] = [];
  selectedLang: string;

  constructor(
    private router: Router,
    private authService: AuthService,
    private translate: TranslateService
  ) {
    this.tipoUsuario = this.authService.getTipoUsuarioFromToken();
    this.selectedLang = localStorage.getItem('lang') || 'es';
  }
ngOnInit(): void {
    this.setSidebarItems();
    this.translate.onLangChange.subscribe(() => {
      this.setSidebarItems();
    });
  }

  private setSidebarItems(): void {
    this.items = [
      {
        label: this.translate.instant('SIDEBAR.DASHBOARD'),
        icon: 'pi pi-home',
        routerLink: ['/dashboard']
      },
      ...(this.tipoUsuario === 'ADMINISTRADOR'
        ? [{
            label: this.translate.instant('SIDEBAR.EDUCADORES'),
            icon: 'pi pi-user-edit',
            routerLink: ['/educadores']
          }]
        : []),
      {
        label: this.translate.instant('SIDEBAR.NINOS'),
        icon: 'pi pi-star',
        routerLink: ['/ninos']
      },
      {
        label: this.translate.instant('SIDEBAR.CLASES'),
        icon: 'pi pi-users',
        routerLink: ['/clases']
      },
      {
        label: this.translate.instant('SIDEBAR.PADRES'),
        icon: 'pi pi-plus',
        routerLink: ['/padres']
      },
      {
        label: this.translate.instant('SIDEBAR.ASISTENCIA'),
        icon: 'pi pi-clock',
        routerLink: ['/asistencias']
      },
      {
        label: this.translate.instant('SIDEBAR.COMIDAS'),
        icon: 'pi pi-apple',
        routerLink: ['/comidas']
      },
      {
        label: this.translate.instant('SIDEBAR.HIGIENES'),
        icon: 'pi pi-shield',
        routerLink: ['/higienes']
      },
      {
        label: this.translate.instant('SIDEBAR.SIESTAS'),
        icon: 'pi pi-moon',
        routerLink: ['/siestas']
      },
      {
        label: this.translate.instant('SIDEBAR.ACTIVIDADES'),
        icon: 'pi pi-book',
        routerLink: ['/actividades']
      }
    ];
  }

  cambiarIdioma(event: any): void {
    const lang = event.value;
    localStorage.setItem('lang', lang);
    this.translate.use(lang);
  }

  irAlPerfil() {
    this.router.navigate(['/profile']);
  }

  cerrarSesion() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}