import { Component, OnInit } from '@angular/core';
import { ChatComponent } from '@components/chat/chat.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MensajesService } from '@services/mensaje/mensajes.service';
import { AuthService } from '@services/auth/auth.service';
import { ParentService } from '@services/parent/parent.service';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-mensajes',
  templateUrl: './mensajes.component.html',
  styleUrls: ['./mensajes.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    ChatComponent,
    DialogModule,
    DropdownModule,
    ButtonModule,
    TranslateModule
  ]
})
export class MensajesComponent implements OnInit {
  conversaciones: any[] = [];
  chatActivo: { chatId: number, receptorId: number, nombre: string } = {
    chatId: 0,
    receptorId: 0,
    nombre: ''
  };

  dialogNuevaConversacion: boolean = false;
  nuevoReceptorId: number | null = null;
  primerMensaje: string = '';
  listaPadres: any[] = [];

  constructor(
    private mensajesService: MensajesService,
    private authService: AuthService,
    private parentService: ParentService

  ) {}

  ngOnInit(): void {
    this.cargarConversaciones();
    this.cargarPadres();
  }

  cargarConversaciones(): void {
    const usuarioActualId = this.authService.getUserIdFromToken();

    if (usuarioActualId !== null) {
      this.mensajesService.getMensajesDeUsuario(usuarioActualId).subscribe({
        next: (data) => {
          this.conversaciones = this.obtenerConversacionesUnicas(data, usuarioActualId);
        },
        error: (error) => {
          console.error('Error al obtener las conversaciones:', error);
        }
      });
    }
  }

  cargarPadres(): void {
    this.parentService.getParents().subscribe({
      next: (padres) => {
        this.listaPadres = padres.map(p => ({
          ...p,
          nombreCompleto: `${p.nombre} ${p.apellidos}`
        }));
      },
      error: (error) => {
        console.error('Error al cargar la lista de padres:', error);
      }
    });
  }
  
  obtenerConversacionesUnicas(mensajes: any[], usuarioId: number): { receptorId: number, nombre: string }[] {
    const conversacionesMap = new Map<number, string>();

    mensajes.forEach(mensaje => {
      const otroUsuarioId = mensaje.emisorId === usuarioId ? mensaje.receptorId : mensaje.emisorId;
      const otroUsuarioNombre = mensaje.emisorId === usuarioId ? mensaje.receptorNombre : mensaje.emisorNombre;

      if (!conversacionesMap.has(otroUsuarioId)) {
        conversacionesMap.set(otroUsuarioId, otroUsuarioNombre);
      }
    });

    return Array.from(conversacionesMap.entries()).map(([receptorId, nombre]) => ({ receptorId, nombre }));
  }

  abrirChat(conversacion: any): void {
    this.chatActivo = conversacion;
  }

  abrirNuevaConversacion(): void {
    this.dialogNuevaConversacion = true;
  }

  enviarPrimerMensaje(): void {
    const emisorId = this.authService.getUserIdFromToken();

    let receptorId: number | null = null;
    if (this.nuevoReceptorId !== null) {
      if (typeof this.nuevoReceptorId === 'object' && 'id' in this.nuevoReceptorId) {
        receptorId = (this.nuevoReceptorId as { id: number }).id;
      } else {
        receptorId = this.nuevoReceptorId;
      }
    }

    if (emisorId !== null && receptorId !== null && this.primerMensaje.trim() !== '') {
      const nuevoMensaje = {
        contenido: this.primerMensaje,
        emisor: { id: emisorId, tipoUsuario: 'EDUCADOR' },
        receptor: { id: receptorId, tipoUsuario: 'PADRE' },
        estado: 'NO_LEIDO'
      };

      console.log('Enviando nuevo mensaje:', nuevoMensaje);

      this.mensajesService.sendMensaje(nuevoMensaje).subscribe({
        next: () => {
          this.cargarConversaciones();
          this.chatActivo = { chatId: 0, receptorId: receptorId!, nombre: 'Nuevo contacto' };
          this.dialogNuevaConversacion = false;
          this.primerMensaje = '';
        },
        error: (error) => {
          console.error('Error al enviar el primer mensaje:', error);
        }
      });
    }
  }
}
