import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { MenuModule } from 'primeng/menu';
import { Router } from '@angular/router';

@Component({
  selector: 'app-topbar',
  imports: [
    InputTextModule,
    AvatarModule,
    ButtonModule,
    MenuModule,
    CommonModule
  ],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.scss'
})
export class TopbarComponent {

  constructor(private router: Router) {}

  items: MenuItem[] = [
    { label: 'Profile', icon: 'pi pi-user', command: () => console.log('Profile') },
    { label: 'Cerrar sesión', icon: 'pi pi-sign-out', command: () => this.logout() },
  ];

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
