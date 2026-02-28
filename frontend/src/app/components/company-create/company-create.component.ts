import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CompanyService } from '../../services/company.service';
import { EmployeeService } from '../../services/employee.service';
import { CepService } from '../../services/cep.service';
import { CepResponse } from '../../models/cep.model';
import { Employee } from '../../models/employee.model';
import { Page } from '../../models/page.model';
import { formatCpf } from '../../utils/format-cpf';
import { formatCnpj } from '../../utils/format-cnpj';

@Component({
  selector: 'app-company-create',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './company-create.component.html',
  styleUrl: './company-create.component.css',
})
export class CompanyCreateComponent {
  companyName = '';
  cnpj = '';
  cep = '';
  state = '';
  cepInfo: CepResponse | null = null;
  cepError = '';
  cepLoading = false;

  selectedEmployees: Employee[] = [];
  showSearch = false;
  searchName = '';
  employeeResults: Employee[] = [];
  searching = false;
  empPage = 0;
  empTotalPages = 0;

  submitting = false;
  error = '';

  constructor(
    private router: Router,
    private companyService: CompanyService,
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

  toggleSearch(): void {
    this.showSearch = !this.showSearch;
    if (this.showSearch) {
      this.empPage = 0;
      this.searchName = '';
      this.loadEmployees();
    }
  }

  loadEmployees(): void {
    this.searching = true;
    const name = this.searchName.trim() || undefined;
    this.employeeService.getEmployees(this.empPage, 5, name).subscribe({
      next: (data: Page<Employee>) => {
        this.employeeResults = data.content;
        this.empTotalPages = data.totalPages;
        this.searching = false;
      },
      error: () => {
        this.searching = false;
      },
    });
  }

  onSearchChange(): void {
    this.empPage = 0;
    this.loadEmployees();
  }

  goToEmpPage(n: number): void {
    this.empPage = n;
    this.loadEmployees();
  }

  get empPages(): number[] {
    return Array.from({ length: this.empTotalPages }, (_, i) => i);
  }

  isSelected(employeeId: number): boolean {
    return this.selectedEmployees.some((e) => e.id === employeeId);
  }

  addEmployee(employee: Employee): void {
    if (!this.isSelected(employee.id)) {
      this.selectedEmployees = [...this.selectedEmployees, employee];
    }
  }

  removeEmployee(employeeId: number): void {
    this.selectedEmployees = this.selectedEmployees.filter(
      (e) => e.id !== employeeId,
    );
  }

  getDocumentFromEmployee(employee: Employee): string {
    if (employee.type === 'INDIVIDUAL') {
      return formatCpf(employee.cpf);
    } else {
      return formatCnpj(employee.cnpj);
    }
  }

  formatEmployeeType(type: string): string {
    if (type === 'INDIVIDUAL') {
      return 'Pessoa Fisica';
    } else if (type === 'LEGAL_ENTITY') {
      return 'Pessoa Juridica';
    } else {
      return type;
    }
  }

  get isFormValid(): boolean {
    return (
      this.companyName.trim().length > 0 &&
      this.cnpj.replace(/\D/g, '').length === 14 &&
      this.cep.replace(/\D/g, '').length === 8 &&
      this.state.length > 0
    );
  }

  onSubmit(): void {
    if (!this.isFormValid || this.submitting) return;

    this.submitting = true;
    this.error = '';

    const employeeIds = this.selectedEmployees.map((e) => e.id);

    this.companyService
      .createCompany({
        companyName: this.companyName.trim(),
        cnpj: this.cnpj.replace(/\D/g, ''),
        cep: this.cep.replace(/\D/g, ''),
        state: this.state,
        employeeId: employeeIds,
      })
      .subscribe({
        next: (company) => {
          this.submitting = false;
          this.router.navigate(['/companies', company.id]);
        },
        error: (err) => {
          this.submitting = false;
          this.error =
            err.error?.error || 'Falha ao criar empresa. Tente novamente.';
        },
      });
  }
}
