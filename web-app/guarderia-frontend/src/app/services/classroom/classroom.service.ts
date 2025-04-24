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
}