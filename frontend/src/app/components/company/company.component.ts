import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';
import { Page } from '../../models/page.model';
import { formatCnpj } from '../../utils/format-cnpj';

@Component({
  selector: 'app-company',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './company.component.html',
  styleUrl: './company.component.css',
})
export class CompanyComponent implements OnInit {
  companies: Company[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  totalElements = 0;

  searchName = '';
  searchCnpj = '';

  loading = false;
  error = '';

  constructor(private companyService: CompanyService) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.loading = true;
    this.error = '';

    const name = this.searchName.trim() || undefined;
    const cnpj = this.searchCnpj.trim().replace(/\D/g, '') || undefined;

    this.companyService
      .getCompanies(this.page, this.size, name, cnpj)
      .subscribe({
        next: (data: Page<Company>) => {
          console.log(data);
          this.companies = data.content;
          this.totalPages = data.totalPages;
          this.totalElements = data.totalElements;
          this.loading = false;
        },
        error: () => {
          this.error =
            'Failed to load companies. Make sure the backend is running.';
          this.loading = false;
        },
      });
  }

  onSearch(): void {
    this.page = 0;
    this.loadCompanies();
  }

  goToPage(n: number): void {
    this.page = n;
    this.loadCompanies();
  }

  deleteCompany(id: number): void {
    if (!confirm('Are you sure you want to delete this company?')) {
      return;
    }

    this.companyService.deleteCompany(id).subscribe({
      next: () => this.loadCompanies(),
      error: () => {
        this.error = 'Failed to delete company.';
      },
    });
  }

  formatCnpj(cnpj: string): string {
    return formatCnpj(cnpj);
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}
