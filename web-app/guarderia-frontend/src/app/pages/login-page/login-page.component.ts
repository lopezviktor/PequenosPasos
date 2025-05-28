import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '@services/auth/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule
  ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService
  ) {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) return;
    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: () => {
        const tipoUsuario = this.authService.getTipoUsuarioFromToken();
        this.messageService.add({
          severity: 'success',
          summary: 'Login exitoso',
          detail: `Bienvenido, ${tipoUsuario?.toLowerCase()}`,
          life: 3000
        });
        this.router.navigate(['/dashboard']);
      },
      error: error => {
        console.error('Login failed', error);

        if (error.status === 0) {
          // El servidor no está disponible o error de red
          this.messageService.add({
            severity: 'error',
            summary: 'Error de conexión',
            detail: 'No se puede conectar con el servidor. Intenta más tarde.',
            life: 4000
          });
        } else if (error.status === 401) {
          // Credenciales incorrectas
          this.messageService.add({
            severity: 'error',
            summary: 'Login fallido',
            detail: 'Credenciales incorrectas. Intenta de nuevo.',
            life: 4000
          });
        } else {
          // Otro error inesperado
          this.messageService.add({
            severity: 'error',
            summary: 'Error inesperado',
            detail: 'Se produjo un error. Intenta más tarde.',
            life: 4000
          });
        }

        this.loginForm.get('password')?.reset();
      }
    });
  }
}
