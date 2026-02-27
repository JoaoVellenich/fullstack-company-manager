import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, CreateEmployeeRequest } from '../models/employee.model';
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

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/${id}`);
  }

  createEmployee(body: CreateEmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.baseUrl, body);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  addCompanyToEmployee(employeeId: number, companyIds: number[]): Observable<Employee> {
    return this.http.put<Employee>(`${this.baseUrl}/${employeeId}/company/add`, {
      companyId: companyIds,
    });
  }
}
