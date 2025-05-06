import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Asistencia } from '@models/asistencia.model';
import { environment } from '../../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AsistenciaService {

  constructor(private http: HttpClient) {}

    private apiUrl = `${environment.apiUrl}/asistencias`;
  
  // Obtener todas las asistencias
  getAsistencias(): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(this.apiUrl);
  }

  // Obtener asistencias por el ID del niño
  getAsistenciasByNinoId(ninoId: number): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(`${this.apiUrl}/nino/${ninoId}`);
  }

  // Obtener una asistencia por ID
  getAsistenciaById(id: number): Observable<Asistencia> {
    return this.http.get<Asistencia>(`${this.apiUrl}/${id}`);
  }

  // Crear una nueva asistencia
  createAsistencia(asistencia: Asistencia): Observable<Asistencia> {
    return this.http.post<Asistencia>(this.apiUrl, asistencia);
  }

  // Actualizar una asistencia
  updateAsistencia(id: number, asistencia: Asistencia): Observable<Asistencia> {
    return this.http.put<Asistencia>(`${this.apiUrl}/${id}`, asistencia);
  }

  // Eliminar una asistencia
  deleteAsistencia(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}