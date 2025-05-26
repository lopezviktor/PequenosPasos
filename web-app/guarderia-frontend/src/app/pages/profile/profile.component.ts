import { Component } from '@angular/core';
import { Educator } from '@models/educator.model';
import { OnInit } from '@angular/core';
import { AuthService } from '@services/auth/auth.service';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CardModule, 
    DialogModule, 
    FormsModule,
    ButtonModule,
    TranslateModule
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  educador!: Educator;

  mostrarDialogoCambioPassword = false;
  nuevaContrasena = '';
  confirmarContrasena = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getAuthenticatedUser().subscribe({
      next: (data) => {
        this.educador = data;
      },
      error: (error) => {
        console.error('Error al obtener el perfil del educador:', error);
      }
    });
  }

  cambiarContrasena(): void {
    if (this.nuevaContrasena !== this.confirmarContrasena) {
      alert('Las contraseñas no coinciden.');
      return;
    }

    // Aquí iría la lógica para enviar la nueva contraseña al backend
    console.log(`Nueva contraseña para ${this.educador.email}: ${this.nuevaContrasena}`);

    this.nuevaContrasena = '';
    this.confirmarContrasena = '';
    this.mostrarDialogoCambioPassword = false;
  }
}
