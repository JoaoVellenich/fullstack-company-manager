import { Employee } from './employee.model';

export interface Company {
  id: number;
  cnpj: string;
  companyName: string;
  cep: string;
  state: string;
  employees: Employee[];
}

export interface CreateCompanyRequest {
  cnpj: string;
  companyName: string;
  cep: string;
  state: string;
  employeeId: number[];
}
