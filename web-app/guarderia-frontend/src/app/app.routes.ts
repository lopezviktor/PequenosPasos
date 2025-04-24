import { Routes } from '@angular/router';
import { MainLayoutComponent } from '@components/layout/main-layout/main-layout.component';
import { DashboardPageComponent } from '@pages/dashboard-page/dashboard-page.component';
import { ParentsPageComponent } from '@pages/parents-page/parents-page.component';
import { EducatorsPageComponent } from '@pages/educators-page/educators-page.component';
import { ChildrenPageComponent } from '@pages/children-page/children-page.component';
import { authGuard } from '@guards/auth.guard';

export const routes: Routes = [
    {
      path: '',
      component: MainLayoutComponent, // incluye sidebar, topbar y un <router-outlet>
      canActivateChild: [authGuard],
      children: [
        { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
        { path: 'dashboard', loadComponent: () => import('./pages/dashboard-page/dashboard-page.component').then(m => m.DashboardPageComponent) },
        { path: 'padres', loadComponent: () => import('./pages/parents-page/parents-page.component').then(m => m.ParentsPageComponent) },
        { path: 'educadores', loadComponent: () => import('./pages/educators-page/educators-page.component').then(m => m.EducatorsPageComponent) },
        { path: 'ninos', loadComponent: () => import('./pages/children-page/children-page.component').then(m => m.ChildrenPageComponent) },
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
