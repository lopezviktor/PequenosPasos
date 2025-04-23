import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parent } from '@models/parent.model';
import { environment } from 'environment/environment';

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
}
