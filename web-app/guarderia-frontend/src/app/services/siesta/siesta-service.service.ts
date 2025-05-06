import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Siesta } from '@models/siesta.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SiestaService {
  private apiUrl = 'http://localhost:8080/api/siestas';

  constructor(private http: HttpClient) {}

  getSiestas(): Observable<Siesta[]> {
    return this.http.get<Siesta[]>(this.apiUrl);
  }

  getSiestaById(id: number): Observable<Siesta> {
    return this.http.get<Siesta>(`${this.apiUrl}/${id}`);
  }

  getSiestasByNinoId(ninoId: number): Observable<Siesta[]> {
    return this.http.get<Siesta[]>(`${this.apiUrl}/nino/${ninoId}`);
  }

  getSiestasByEducadorId(educadorId: number): Observable<Siesta[]> {
    return this.http.get<Siesta[]>(`${this.apiUrl}/educador/${educadorId}`);
  }

  create(siesta: Siesta): Observable<Siesta> {
    return this.http.post<Siesta>(this.apiUrl, siesta);
  }

  update(id: number, siesta: Siesta): Observable<Siesta> {
    return this.http.put<Siesta>(`${this.apiUrl}/${id}`, siesta);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}