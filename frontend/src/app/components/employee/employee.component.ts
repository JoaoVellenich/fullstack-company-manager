import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';
import { Page } from '../../models/page.model';
import { formatCpf } from '../../utils/format-cpf';
import { formatCnpj } from '../../utils/format-cnpj';

@Component({
  selector: 'app-employee',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.css',
})
export class EmployeeComponent implements OnInit {
  employees: Employee[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;

  searchName = '';
  searchDocument = '';

  loading = false;
  error = '';

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.error = '';

    const name = this.searchName.trim() || undefined;
    const document = this.searchDocument.trim().replace(/\D/g, '') || undefined;

    this.employeeService
      .getEmployees(this.page, this.size, name, document)
      .subscribe({
        next: (data: Page<Employee>) => {
          this.employees = data.content;
          this.totalPages = data.totalPages;
          this.totalElements = data.totalElements;
          this.loading = false;
        },
        error: () => {
          this.error =
            'Failed to load employees. Make sure the backend is running.';
          this.loading = false;
        },
      });
  }

  onSearch(): void {
    this.page = 0;
    this.loadEmployees();
  }

  goToPage(n: number): void {
    this.page = n;
    this.loadEmployees();
  }

  deleteEmployee(id: number): void {
    if (!confirm('Are you sure you want to delete this employee?')) {
      return;
    }

    this.employeeService.deleteEmployee(id).subscribe({
      next: () => this.loadEmployees(),
      error: () => {
        this.error = 'Failed to delete employee.';
      },
    });
  }

  getDocument(employee: Employee): string {
    if (employee.type === 'INDIVIDUAL') {
      return formatCpf(employee.cpf);
    } else {
      return formatCnpj(employee.cnpj);
    }
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

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
