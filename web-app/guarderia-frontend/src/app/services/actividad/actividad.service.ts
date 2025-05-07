import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'; 
import { Actividad, ActividadConDetalles } from '@models/actividad.model';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActividadService {

  private apiUrl = `${environment.apiUrl}/actividades`;
  private apiActividadNinosUrl = `${environment.apiUrl}/actividad-ninos`;

  constructor(private http: HttpClient) { }

  // Obtener todas las actividades (solo datos básicos)
  getActividades(): Observable<Actividad[]> {
      return this.http.get<Actividad[]>(`${this.apiUrl}`).pipe(
          map((actividades) => {
              console.log('Datos básicos recibidos del backend:', actividades);
              return actividades.map((actividad) => ({
                  id: actividad.actividadId,
                  nombre: actividad.nombre,
                  descripcion: actividad.descripcion
              }));
          })
      );
  }

  // Obtener detalles completos de una actividad para edición
  getActividadConDetalles(id: number): Observable<ActividadConDetalles> {
      return this.http.get<ActividadConDetalles>(`${this.apiUrl}/${id}/detalles`).pipe(
          map((actividad) => {
              console.log('Detalles de la actividad recibidos:', actividad);
              return {
                  ...actividad,
                  educadorNombre: `${actividad?.educador?.nombre} ${actividad?.educador?.apellidos}`,
                  claseNombre: actividad?.clase?.nombre,
                  ninos: actividad?.ninos?.map((nino) => ({
                      id: nino.id,
                      nombre: nino.nombre,
                      apellidos: nino.apellidos,
                      fechaNacimiento: nino.fechaNacimiento,
                      primerDia: nino.primerDia
                  })) ?? []
              };
          })
      );
  }

  // Obtener actividad por ID
  getById(id: number): Observable<Actividad> {
    return this.http.get<Actividad>(`${this.apiUrl}/${id}`);
  }

  // Crear nueva actividad
  create(actividad: Actividad): Observable<Actividad> {
    return this.http.post<Actividad>(this.apiUrl, actividad);
  }

  // Actualizar actividad existente
  update(id: number, actividad: Actividad): Observable<Actividad> {
    return this.http.put<Actividad>(`${this.apiUrl}/${id}`, actividad);
  }

  // Eliminar actividad
  deleteActividad(id: number): Observable<void> {
    console.log('Eliminando actividad con ID:', id);
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
}

  // Añadir niño a una actividad
  addNinoToActividad(actividadId: number, ninoId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${actividadId}/nino/${ninoId}`, {});
  }

  // Eliminar niño de una actividad
  removeNinoFromActividad(actividadId: number, ninoId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${actividadId}/nino/${ninoId}`);
  }

  // Añadir una clase completa a una actividad
  addClaseToActividad(actividadId: number, claseId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${actividadId}/clase/${claseId}`, {});
  }

  // Registrar actividad para una clase completa
  registrarActividadPorClase(claseId: number, actividad: Actividad): Observable<void> {
    const payload = { claseId, actividadSimplificada: actividad };
    return this.http.post<void>(`${this.apiActividadNinosUrl}/clase`, payload);
  }

  // Registrar actividad para niños individuales
  registrarActividadIndividual(actividadId: number, ninoIds: number[]): Observable<void> {
    const payload = { actividadId, ninoIds };
    return this.http.post<void>(`${this.apiActividadNinosUrl}`, payload);
  }
}
