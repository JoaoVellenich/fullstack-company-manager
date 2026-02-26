import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee } from '../models/employee.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  private readonly baseUrl =
    'http://localhost:8080/companyManager/api/employees';

  constructor(private http: HttpClient) {}

  getEmployees(
    page: number,
    size: number,
    name?: string,
    document?: string,
  ): Observable<Page<Employee>> {
    let params = new HttpParams().set('page', page).set('size', size);

    if (name) {
      params = params.set('name', name);
    }
    if (document) {
      params = params.set('document', document);
    }

    return this.http.get<Page<Employee>>(this.baseUrl, { params });
  }
}
