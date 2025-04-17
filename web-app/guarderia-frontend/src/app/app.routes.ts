import { Routes } from '@angular/router';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import { ParentsPageComponent } from './pages/parents-page/parents-page.component';

export const routes: Routes = [
    {
        path: '',
        component: DashboardPageComponent
    },
    {
        path: 'padres',
        component: ParentsPageComponent
    }
];
