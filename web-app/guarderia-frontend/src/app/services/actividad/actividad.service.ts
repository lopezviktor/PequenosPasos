import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, switchMap } from 'rxjs/operators'; 
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


  // Obtener datos básicos de la actividad
  getActividad(id: number): Observable<Actividad> {
    const actividadUrl = `${this.apiUrl}/${id}`;
    return this.http.get<Actividad>(actividadUrl);
  }

  // Obtener todos los detalles de la actividad: clase, educador y lista de niños
  getActividadDetalles(id: number): Observable<any> {
    const actividadNinosUrl = `${this.apiActividadNinosUrl}/actividad/${id}`;
    return this.http.get<any[]>(actividadNinosUrl).pipe(
      map((actividadNinos) => {
        if (actividadNinos.length === 0) {
          return {
            claseNombre: 'Sin clase',
            educadorNombre: 'Desconocido',
            ninos: []
          };
        }

        const educador = actividadNinos[0].nino.clase.educador;
        const clase = actividadNinos[0].nino.clase.nombre;
        const ninos = actividadNinos.map((actividadNino) => ({
          id: actividadNino.nino.id,
          nombre: actividadNino.nino.nombre,
          apellidos: actividadNino.nino.apellidos,
        }));

        return {
          claseNombre: clase,
          educadorNombre: `${educador.nombre} ${educador.apellidos}`,
          ninos: ninos
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
  registrarActividadPorClase(claseId: number, actividadId: number): Observable<any> {
    const payload = {
      claseId: claseId,
      actividad: {
        actividadId: actividadId
      }
    };
    return this.http.post(`${this.apiActividadNinosUrl}/clase`, payload);
  }

  // Registrar actividad para niños individuales
  registrarActividadIndividual(actividadId: number, ninoIds: number[]): Observable<void> {
    const payload = {
      actividadId: actividadId,
      ninoIds: ninoIds
    };
    return this.http.post<void>(`${this.apiActividadNinosUrl}`, payload);
  }
}
