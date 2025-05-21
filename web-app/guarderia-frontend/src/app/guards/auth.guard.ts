import { CanActivateChildFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '@services/auth/auth.service';
import { MessageService } from 'primeng/api';

export const authGuard: CanActivateChildFn = (childRoute, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);
  const messageService = inject(MessageService);

  const token = localStorage.getItem('token');

  if (token) {
    const tipoUsuario = authService.getTipoUsuarioFromToken();
    if (tipoUsuario === 'ADMINISTRADOR' || tipoUsuario === 'EDUCADOR') {
      return true;
    } else {
      router.navigate(['/login']);
      return false;
    }
  } else {
    router.navigate(['/login']);
    return false;
  }
};
