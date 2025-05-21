import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '@services/auth/auth.service';
import { MessageService } from 'primeng/api';

export const adminOnlyGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const messageService = inject(MessageService);

  const tipoUsuario = authService.getTipoUsuarioFromToken();

  if (tipoUsuario === 'ADMINISTRADOR') {
    return true;
  } else {
    messageService.add({
      severity: 'error',
      summary: 'Acceso denegado',
      detail: 'Solo el administrador puede acceder a esta sección.',
      life: 4000
    });
    router.navigate(['/dashboard']);
    return false;
  }
};