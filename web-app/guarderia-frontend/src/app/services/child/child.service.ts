import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Child } from '../../models/child.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChildService {
  private apiUrl = `${environment.apiUrl}/ninos`;

  constructor(private http: HttpClient) {}

  getAllChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(this.apiUrl);
  }

  getChildById(id: number): Observable<Child> {
    return this.http.get<Child>(`${this.apiUrl}/${id}`);
  }

  createChild(child: Child): Observable<Child> {
    return this.http.post<Child>(this.apiUrl, child);
  }

  updateChild(child: Child): Observable<Child> {
    return this.http.put<Child>(`${this.apiUrl}/${child.id}`, child);
  }

  deleteChild(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getChildrenByClass(claseId: number): Observable<Child[]> {
    return this.http.get<Child[]>(`${this.apiUrl}/clase/${claseId}`);
  }
}
