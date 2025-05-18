import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Clase } from '../../models/clase.model';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClassroomService {

  private apiUrl = `${environment.apiUrl}/clases`;

  constructor(private http: HttpClient) {}

  getClases(): Observable<Clase[]> {
    return this.http.get<Clase[]>(this.apiUrl);
  }

  getClaseById(id: number): Observable<Clase> {
    return this.http.get<Clase>(`${this.apiUrl}/${id}`);
  }

  crearClase(clase: Clase): Observable<Clase> {
    return this.http.post<Clase>(this.apiUrl, clase);
  }
  
  actualizarClase(clase: Clase): Observable<Clase> {
    return this.http.put<Clase>(`${this.apiUrl}/${clase.id}`, clase);
  }
  
  deleteClase(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
}