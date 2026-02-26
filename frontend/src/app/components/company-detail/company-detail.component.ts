import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CompanyService } from '../../services/company.service';
import { EmployeeService } from '../../services/employee.service';
import { Company } from '../../models/company.model';
import { Employee } from '../../models/employee.model';
import { Page } from '../../models/page.model';
import { formatCpf } from '../../utils/format-cpf';
import { formatCnpj } from '../../utils/format-cnpj';

@Component({
  selector: 'app-company-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './company-detail.component.html',
  styleUrl: './company-detail.component.css',
})
export class CompanyDetailComponent implements OnInit {
  company: Company | null = null;
  loading = true;
  error = '';

  showSearch = false;
  searchName = '';
  employeeResults: Employee[] = [];
  searching = false;
  empPage = 0;
  empTotalPages = 0;

  constructor(
    private route: ActivatedRoute,
    private companyService: CompanyService,
    private employeeService: EmployeeService,
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCompany(id);
  }

  loadCompany(id: number): void {
    this.loading = true;
    this.companyService.getCompanyById(id).subscribe({
      next: (company) => {
        this.company = company;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load company.';
        this.loading = false;
      },
    });
  }

  formatCnpj(cnpj: string): string {
    return formatCnpj(cnpj);
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

  isAlreadyInCompany(employeeId: number): boolean {
    if (!this.company) return false;
    return this.company.employees.some((e) => e.id === employeeId);
  }

  addEmployee(employeeId: number): void {
    if (!this.company) return;
    this.companyService
      .addEmployeeToCompany(this.company.id, [employeeId])
      .subscribe({
        next: (updated) => {
          this.company = updated;
        },
        error: () => {
          this.error = 'Failed to add employee.';
        },
      });
  }

  getDocumentFromEmployee(employee: Employee): string {
    if (employee.type == 'INDIVIDUAL') {
      return formatCpf(employee.cpf);
    } else {
      return formatCnpj(employee.cnpj);
    }
  }

  formatEmployeeTYpe(type: string): string {
    if (type == 'INDIVIDUAL') {
      return 'Pessoa Física';
    } else if (type == 'LEGAL_ENTITY') {
      return 'Pessoa Jurídica';
    } else {
      return type;
    }
  }

  removeEmployee(employeeId: number): void {
    if (!this.company) return;
    if (
      !confirm(
        'Are you sure you want to remove this employee from the company?',
      )
    )
      return;
    this.companyService
      .removeEmployeeFromCompany(this.company.id, [employeeId])
      .subscribe({
        next: (updated) => {
          this.company = updated;
        },
        error: () => {
          this.error = 'Failed to remove employee.';
        },
      });
  }
}
