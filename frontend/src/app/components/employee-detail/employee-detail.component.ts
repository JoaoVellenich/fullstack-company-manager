import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { CompanyService } from '../../services/company.service';
import { Employee } from '../../models/employee.model';
import { Company } from '../../models/company.model';
import { Page } from '../../models/page.model';
import { formatCpf } from '../../utils/format-cpf';
import { formatCnpj } from '../../utils/format-cnpj';

@Component({
  selector: 'app-employee-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './employee-detail.component.html',
  styleUrl: './employee-detail.component.css',
})
export class EmployeeDetailComponent implements OnInit {
  employee: Employee | null = null;
  loading = true;
  error = '';

  showSearch = false;
  searchName = '';
  companyResults: Company[] = [];
  searching = false;
  compPage = 0;
  compTotalPages = 0;

  constructor(
    private route: ActivatedRoute,
    private employeeService: EmployeeService,
    private companyService: CompanyService,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadEmployee(id);
  }

  loadEmployee(id: number): void {
    this.loading = true;
    this.employeeService.getEmployeeById(id).subscribe({
      next: (employee) => {
        this.employee = employee;
        this.loading = false;
      },
      error: () => {
        this.error = 'Falha ao carregar funcionário.';
        this.loading = false;
      },
    });
  }

  getDocument(): string {
    if (!this.employee) return '';
    if (this.employee.type === 'INDIVIDUAL') {
      return formatCpf(this.employee.cpf);
    } else {
      return formatCnpj(this.employee.cnpj);
    }
  }

  getDocumentLabel(): string {
    if (!this.employee) return 'Documento';
    return this.employee.type === 'INDIVIDUAL' ? 'CPF' : 'CNPJ';
  }

  formatType(type: string): string {
    if (type === 'INDIVIDUAL') {
      return 'Pessoa Física';
    } else if (type === 'LEGAL_ENTITY') {
      return 'Pessoa Jurídica';
    } else {
      return type;
    }
  }

  formatCnpj(cnpj: string): string {
    return formatCnpj(cnpj);
  }

  toggleSearch(): void {
    this.showSearch = !this.showSearch;
    if (this.showSearch) {
      this.compPage = 0;
      this.searchName = '';
      this.loadCompanies();
    }
  }

  loadCompanies(): void {
    this.searching = true;
    const name = this.searchName.trim() || undefined;
    this.companyService.getCompanies(this.compPage, 5, name).subscribe({
      next: (data: Page<Company>) => {
        this.companyResults = data.content;
        this.compTotalPages = data.totalPages;
        this.searching = false;
      },
      error: () => {
        this.searching = false;
      },
    });
  }

  onSearchChange(): void {
    this.compPage = 0;
    this.loadCompanies();
  }

  goToCompPage(n: number): void {
    this.compPage = n;
    this.loadCompanies();
  }

  get compPages(): number[] {
    return Array.from({ length: this.compTotalPages }, (_, i) => i);
  }

  isAlreadyInEmployee(companyId: number): boolean {
    if (!this.employee) return false;
    return this.employee.companies.some((c) => c.id === companyId);
  }

  addCompany(companyId: number): void {
    if (!this.employee) return;
    this.employeeService
      .addCompanyToEmployee(this.employee.id, [companyId])
      .subscribe({
        next: (updated) => {
          this.employee = updated;
        },
        error: () => {
          this.error = 'Falha ao adicionar empresa.';
        },
      });
  }

  removeCompany(companyId: number): void {
    if (!this.employee) return;
    if (
      !confirm(
        'Tem certeza que deseja remover esta empresa do funcionário?',
      )
    )
      return;
    this.companyService
      .removeEmployeeFromCompany(companyId, [this.employee.id])
      .subscribe({
        next: () => {
          this.loadEmployee(this.employee!.id);
        },
        error: () => {
          this.error = 'Falha ao remover empresa.';
        },
      });
  }
}
