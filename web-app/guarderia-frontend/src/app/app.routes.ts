import { Routes } from '@angular/router';
import { MainLayoutComponent } from '@components/layout/main-layout/main-layout.component';
import { authGuard } from '@guards/auth.guard';
import { adminOnlyGuard } from '@guards/admin-only/admin-only.guard';

export const routes: Routes = [
    {
      path: '',
      component: MainLayoutComponent, 
      canActivateChild: [authGuard],
      children: [
        { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
        { path: 'profile', loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent) },
        { path: 'dashboard', loadComponent: () => import('./pages/dashboard-page/dashboard-page.component').then(m => m.DashboardPageComponent) },
        { path: 'padres', loadComponent: () => import('./pages/parents-page/parents-page.component').then(m => m.ParentsPageComponent) },
        {
          path: 'educadores',
          canActivate: [adminOnlyGuard],
          loadComponent: () => import('./pages/educators-page/educators-page.component').then(m => m.EducatorsPageComponent)
        },        
        { path: 'ninos', loadComponent: () => import('./pages/children-page/children-page.component').then(m => m.ChildrenPageComponent) },
        { path: 'clases', loadComponent: () => import('./pages/classes-page/classes-page.component').then(m => m.ClassesPageComponent) },
        { path: 'comidas', loadComponent: () => import('./pages/comidas-page/comidas-page.component').then(m => m.ComidasPageComponent) },
        { path: 'asistencias', loadComponent: () => import('./pages/asistencia-page/asistencia-page.component').then(m => m.AsistenciaPageComponent) },
        { path: 'higienes', loadComponent: () => import('./pages/higienes-page/higienes-page.component').then(m => m.HigienesPageComponent) },
        { path: 'siestas', loadComponent: () => import('./pages/siestas-page/siestas-page.component').then(m => m.SiestasPageComponent) },
        { path: 'actividades', loadComponent: () => import('./pages/actividad-page/actividad-page.component').then(m => m.ActividadPageComponent) },
        { path: 'actividades/:id', loadComponent: () => import('./components/actividad-detail/actividad-detail.component').then(m => m.ActividadDetailComponent) },
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
