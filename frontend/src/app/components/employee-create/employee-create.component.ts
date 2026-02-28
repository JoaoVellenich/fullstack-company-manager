import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { CepService } from '../../services/cep.service';
import { CepResponse } from '../../models/cep.model';

@Component({
  selector: 'app-employee-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './employee-create.component.html',
  styleUrl: './employee-create.component.css',
})
export class EmployeeCreateComponent {
  name = '';
  email = '';
  type: 'INDIVIDUAL' | 'LEGAL_ENTITY' = 'INDIVIDUAL';
  cpf = '';
  rg = '';
  birthDate = '';
  cnpj = '';
  cep = '';
  state = '';
  cepInfo: CepResponse | null = null;
  cepError = '';
  cepLoading = false;

  submitting = false;
  error = '';

  constructor(
    private router: Router,
    private employeeService: EmployeeService,
    private cepService: CepService,
  ) {}

  lookupCep(): void {
    const cleanCep = this.cep.replace(/\D/g, '');
    if (cleanCep.length !== 8) {
      this.cepError = 'O CEP deve ter 8 dígitos.';
      this.cepInfo = null;
      this.state = '';
      return;
    }

    this.cepLoading = true;
    this.cepError = '';
    this.cepService.lookupCep(cleanCep).subscribe({
      next: (data) => {
        this.cepInfo = data;
        this.state = data.uf;
        this.cepLoading = false;
      },
      error: () => {
        this.cepError = 'CEP inválido ou serviço indisponível.';
        this.cepInfo = null;
        this.state = '';
        this.cepLoading = false;
      },
    });
  }

  get isFormValid(): boolean {
    const baseValid =
      this.name.trim().length > 0 &&
      this.cep.replace(/\D/g, '').length === 8 &&
      this.state.length > 0;

    if (this.type === 'INDIVIDUAL') {
      return (
        baseValid &&
        this.cpf.replace(/\D/g, '').length === 11 &&
        this.rg.trim().length > 0 &&
        this.birthDate.length > 0
      );
    } else {
      return baseValid && this.cnpj.replace(/\D/g, '').length === 14;
    }
  }

  onSubmit(): void {
    if (!this.isFormValid || this.submitting) return;

    this.submitting = true;
    this.error = '';

    const body: any = {
      name: this.name.trim(),
      type: this.type,
      email: this.email.trim() || undefined,
      cep: this.cep.replace(/\D/g, ''),
      state: this.state,
    };

    if (this.type === 'INDIVIDUAL') {
      body.cpf = this.cpf.replace(/\D/g, '');
      body.rg = this.rg.trim();
      body.birthDate = this.birthDate;
    } else {
      body.cnpj = this.cnpj.replace(/\D/g, '');
    }

    this.employeeService.createEmployee(body).subscribe({
      next: (employee) => {
        this.submitting = false;
        this.router.navigate(['/employees', employee.id]);
      },
      error: (err) => {
        this.submitting = false;
        this.error =
          err.error?.error || 'Falha ao criar funcionário. Tente novamente.';
      },
    });
  }
}
