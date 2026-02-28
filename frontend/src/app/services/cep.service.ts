import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CepResponse } from '../models/cep.model';
import { appConfig } from '../app-config';

@Injectable({ providedIn: 'root' })
export class CepService {
  private get baseUrl(): string {
    return `${appConfig.apiUrl}/cep`;
  }

  constructor(private http: HttpClient) {}

  lookupCep(cep: string): Observable<CepResponse> {
    return this.http.get<CepResponse>(`${this.baseUrl}/${cep}`);
  }
}
