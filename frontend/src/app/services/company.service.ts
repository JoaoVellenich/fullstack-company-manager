import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Company, CreateCompanyRequest } from '../models/company.model';
import { Page } from '../models/page.model';

@Injectable({ providedIn: 'root' })
export class CompanyService {
  private readonly baseUrl =
    'http://localhost:8080/companyManager/api/companies';

  constructor(private http: HttpClient) {}

  getCompanies(
    page: number,
    size: number,
    name?: string,
    cnpj?: string,
  ): Observable<Page<Company>> {
    let params = new HttpParams().set('page', page).set('size', size);

    if (name) {
      params = params.set('name', name);
    }
    if (cnpj) {
      params = params.set('cnpj', cnpj);
    }

    return this.http.get<Page<Company>>(this.baseUrl, { params });
  }

  createCompany(body: CreateCompanyRequest): Observable<Company> {
    return this.http.post<Company>(this.baseUrl, body);
  }

  deleteCompany(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getCompanyById(id: number): Observable<Company> {
    return this.http.get<Company>(`${this.baseUrl}/${id}`);
  }

  addEmployeeToCompany(companyId: number, employeeIds: number[]): Observable<Company> {
    return this.http.put<Company>(`${this.baseUrl}/${companyId}/employee/add`, {
      employeeId: employeeIds,
    });
  }

  removeEmployeeFromCompany(companyId: number, employeeIds: number[]): Observable<Company> {
    return this.http.put<Company>(`${this.baseUrl}/${companyId}/employee/remove`, {
      employeeId: employeeIds,
    });
  }
}
