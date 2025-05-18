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

  getSiestasDeHoy(): Observable<Siesta[]> {
    const hoy = new Date();

    const inicio = new Date(hoy);
    inicio.setHours(0, 0, 0, 0);

    const fin = new Date(hoy);
    fin.setHours(23, 59, 59, 999);

    const formatDate = (d: Date): string => {
      const pad = (n: number) => n.toString().padStart(2, '0');
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    };

    const inicioStr = formatDate(inicio);
    const finStr = formatDate(fin);

    return this.http.get<Siesta[]>(`${this.apiUrl}/rango-fechas/entidad?inicio=${inicioStr}&fin=${finStr}`);
  }
}