import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comida } from '@models/comida.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ComidaService {

  private apiUrl = 'http://localhost:8080/api/comidas';

  constructor(private http: HttpClient) {}

  // Obtener todas las comidas
  getAll(): Observable<Comida[]> {
    return this.http.get<Comida[]>(this.apiUrl);
  }

  // Obtener una comida por ID
  getById(id: number): Observable<Comida> {
    return this.http.get<Comida>(`${this.apiUrl}/${id}`);
  }

  // Obtener comidas por el ID del niño
  getByNinoId(ninoId: number): Observable<Comida[]> {
    return this.http.get<Comida[]>(`${this.apiUrl}/nino/${ninoId}`);
  }

  // Obtener comidas por el ID del educador
  getByEducadorId(educadorId: number): Observable<Comida[]> {
    return this.http.get<Comida[]>(`${this.apiUrl}/educador/${educadorId}`);
  }

  // Crear una nueva comida
  createComida(comida: Comida): Observable<Comida> {
    return this.http.post<Comida>(this.apiUrl, comida);
  }

  // Actualizar una comida existente (recibe todos los datos)
  updateComida(id: number, comida: Comida): Observable<Comida> {
    return this.http.put<Comida>(`${this.apiUrl}/${id}`, comida);
  }

  // Eliminar una comida por ID
  deleteComida(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}