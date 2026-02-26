import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CompanyComponent } from './components/company/company.component';
import { CompanyDetailComponent } from './components/company-detail/company-detail.component';
import { EmployeeComponent } from './components/employee/employee.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'companies',
    component: CompanyComponent,
  },
  {
    path: 'companies/new',
    component: CompanyComponent,
  },
  {
    path: 'companies/:id',
    component: CompanyDetailComponent,
  },

  {
    path: 'employees',
    component: EmployeeComponent,
  },
];
