import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mensaje } from '../../models/mensaje.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MensajesService {

  private apiUrl = `${environment.apiUrl}/mensajes`;

  constructor(private http: HttpClient) { }

  // Obtener mensajes de un chat
  getMensajes(chatId: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/chat/${chatId}`);
  }

  // Obtener conversaciones del usuario actual (reemplaza la URL si tu backend tiene otra ruta)
  getConversaciones(usuario1: number, usuario2: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/chat/completo?usuario1=${usuario1}&usuario2=${usuario2}`);
  }

  getConversacionCompleta(usuario1: number, usuario2: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/chat/completo?usuario1=${usuario1}&usuario2=${usuario2}`);
  }

  // Obtener todos los mensajes de un usuario (inbox general)
  getMensajesDeUsuario(usuarioId: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  // Obtener los mensajes recibidos por un receptor
  getMensajesRecibidos(receptorId: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/receptor/${receptorId}`);
  }

  // Obtener los mensajes no leídos del receptor
  getMensajesNoLeidos(receptorId: number): Observable<Mensaje[]> {
    return this.http.get<Mensaje[]>(`${this.apiUrl}/receptor/${receptorId}/no-leidos`);
  }

  // Marcar un mensaje como leído
  marcarLeido(mensajeId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${mensajeId}/marcar-leido`, {});
  }

  // Marcar todos los mensajes como leídos para un receptor
  marcarTodosLeidos(receptorId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/receptor/${receptorId}/marcar-todos-leidos`, {});
  }

  // Enviar un nuevo mensaje
  sendMensaje(mensaje: any): Observable<Mensaje> {
    return this.http.post<Mensaje>(this.apiUrl, mensaje);
  }

  // Marcar un mensaje como leído
  markAsRead(mensajeId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${mensajeId}/leido`, {});
  }

  // Eliminar un mensaje
  deleteMensaje(mensajeId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${mensajeId}`);
  }
}
