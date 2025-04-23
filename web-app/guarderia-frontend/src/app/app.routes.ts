import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { DashboardPageComponent } from '@pages/dashboard-page/dashboard-page.component';
import { ParentsPageComponent } from '@pages/parents-page/parents-page.component';

export const routes: Routes = [
    {
      path: '',
      component: MainLayoutComponent, // incluye sidebar, topbar y un <router-outlet>
      children: [
        { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
        { path: 'dashboard', loadComponent: () => import('./pages/dashboard-page/dashboard-page.component').then(m => m.DashboardPageComponent) },
        { path: 'padres', loadComponent: () => import('./pages/parents-page/parents-page.component').then(m => m.ParentsPageComponent) },
      ]
    },
    {
      path: 'login',
      loadComponent: () => import('./pages/login-page/login-page.component').then(m => m.LoginPageComponent)
    },
    {
      path: '**',
      redirectTo: 'login'
    }
  ];
