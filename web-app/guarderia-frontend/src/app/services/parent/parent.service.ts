import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parent } from '@models/parent.model';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ParentService {
  private baseUrl = `${environment.apiUrl}/padres`;

  constructor(private http: HttpClient) {}

  getParents(): Observable<Parent[]> {
    return this.http.get<Parent[]>(this.baseUrl);
  }

  createParent(parent: Parent): Observable<Parent> {
    return this.http.post<Parent>(this.baseUrl, parent);
  }
  
  updateParent(parent: Parent): Observable<Parent> {
    return this.http.put<Parent>(`${this.baseUrl}/${parent.id}`, parent);
  }
  
  deleteParent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

}
