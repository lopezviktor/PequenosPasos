import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comida } from '@models/comida.model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ComidaService {

  private apiUrl = `${environment.apiUrl}/comidas`;

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
  create(comida: Comida): Observable<Comida> {
    return this.http.post<Comida>(this.apiUrl, comida);
  }

  // Actualizar una comida existente (recibe todos los datos)
  update(id: number, comida: Comida): Observable<Comida> {
    return this.http.put<Comida>(`${this.apiUrl}/${id}`, comida);
  }

  // Eliminar una comida por ID
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}