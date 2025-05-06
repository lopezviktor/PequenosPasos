import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Higiene } from '@models/higiene.model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HigieneService {

  private apiUrl = `${environment.apiUrl}/higiene`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Higiene[]> {
    return this.http.get<Higiene[]>(this.apiUrl);
  }

  getById(id: number): Observable<Higiene> {
    return this.http.get<Higiene>(`${this.apiUrl}/${id}`);
  }

  getByNino(ninoId: number): Observable<Higiene[]> {
    return this.http.get<Higiene[]>(`${this.apiUrl}/nino/${ninoId}`);
  }

  getByEducador(educadorId: number): Observable<Higiene[]> {
    return this.http.get<Higiene[]>(`${this.apiUrl}/educador/${educadorId}`);
  }

  create(higiene: Higiene): Observable<Higiene> {
    return this.http.post<Higiene>(this.apiUrl, higiene);
  }

  update(id: number, higiene: Higiene): Observable<Higiene> {
    return this.http.put<Higiene>(`${this.apiUrl}/${id}`, higiene);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}