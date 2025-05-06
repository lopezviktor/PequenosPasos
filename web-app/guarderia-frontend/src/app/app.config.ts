import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { provideAnimations} from '@angular/platform-browser/animations';
import { providePrimeNG } from 'primeng/config'
import Aura from '@primeng/themes/Aura';
import { ConfirmationService } from 'primeng/api';

import { routes } from './app.routes';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';

import { DialogService } from 'primeng/dynamicdialog';
import { MessageService } from 'primeng/api';


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(withInterceptors([AuthInterceptor])),
    providePrimeNG({
      theme: {
        preset: Aura
      }
    }),
    ConfirmationService,
    DialogService,
    MessageService
  ]
};
