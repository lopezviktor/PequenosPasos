import { Injectable, model } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Educator } from '@models/educator.model';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EducatorService {
  private apiUrl = `${environment.apiUrl}/educadores`;

  constructor(private http: HttpClient) {}

  getEducators(): Observable<Educator[]> {
    return this.http.get<Educator[]>(this.apiUrl);
  }

  getEducatorById(id: number): Observable<Educator> {
    return this.http.get<Educator>(`${this.apiUrl}/${id}`);
  }

  createEducator(educator: Educator): Observable<Educator> {
    return this.http.post<Educator>(this.apiUrl, educator);
  }

  updateEducator(educator: Educator): Observable<Educator> {
    return this.http.put<Educator>(`${this.apiUrl}/${educator.id}`, educator);
  }

  deleteEducator(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}