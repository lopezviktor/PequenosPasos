import { Component, Input, OnInit } from '@angular/core';
import { MensajesService } from '../../services/mensaje/mensajes.service';
import { Mensaje } from '../../models/mensaje.model';
import { AuthService } from '../../services/auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat',
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  @Input() chatId!: number;
  @Input() receptorId!: number;
  mensajes: Mensaje[] = [];
  nuevoMensaje = '';

  constructor(
    private mensajesService: MensajesService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarMensajes();
  }

  ngOnChanges(): void {
    if (this.receptorId) {
      this.cargarMensajes();
    }
  }

  cargarMensajes(): void {
    const emisorId = this.authService.getUserIdFromToken();
    if (emisorId !== null && this.receptorId !== null) {
      this.mensajesService.getConversacionCompleta(emisorId, this.receptorId).subscribe((data) => {
        this.mensajes = data;
        setTimeout(() => this.scrollToBottom(), 0);
      });
    }
  }

  enviarMensaje(): void {
    if (!this.nuevoMensaje.trim()) return;

    const emisorId = this.authService.getUserIdFromToken();
    const mensaje = {
      contenido: this.nuevoMensaje,
      emisor: { id: emisorId, tipoUsuario: 'EDUCADOR' },
      receptor: { id: this.receptorId, tipoUsuario: 'PADRE' },
      estado: 'NO_LEIDO'
    };

    this.mensajesService.sendMensaje(mensaje).subscribe({
      next: (nuevo) => {
        this.mensajes.push(nuevo);
        setTimeout(() => this.scrollToBottom(), 0);
        this.nuevoMensaje = '';
      },
      error: (error) => console.error('Error al enviar el mensaje:', error)
    });
  }

  obtenerReceptorId(): number {
    return this.receptorId;
  }

  esMio(mensaje: any): boolean {
    const usuarioActualId = this.authService.getUserIdFromToken(); 
    return mensaje.emisorId === usuarioActualId;
  }

  scrollToBottom(): void {
    setTimeout(() => {
      const container = document.querySelector('.mensajes-lista');
      if (container) {
        container.scrollTop = container.scrollHeight;
      }
    }, 0);
  }
}