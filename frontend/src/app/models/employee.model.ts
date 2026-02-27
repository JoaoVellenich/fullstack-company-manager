import { Company } from './company.model';

export interface Employee {
  id: number;
  name: string;
  cpf: string;
  cnpj: string;
  rg: string;
  birthDate: string;
  type: string;
  email: string;
  cep: string;
  state: string;
  companies: Company[];
}

export interface CreateEmployeeRequest {
  name: string;
  type: string;
  cpf?: string;
  cnpj?: string;
  rg?: string;
  birthDate?: string;
  email?: string;
  cep: string;
  state: string;
}
