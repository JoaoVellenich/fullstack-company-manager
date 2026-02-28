import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CompanyComponent } from './components/company/company.component';
import { CompanyCreateComponent } from './components/company-create/company-create.component';
import { CompanyDetailComponent } from './components/company-detail/company-detail.component';
import { EmployeeComponent } from './components/employee/employee.component';
import { EmployeeDetailComponent } from './components/employee-detail/employee-detail.component';
import { EmployeeCreateComponent } from './components/employee-create/employee-create.component';

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
    component: CompanyCreateComponent,
  },
  {
    path: 'companies/:id',
    component: CompanyDetailComponent,
  },

  {
    path: 'employees',
    component: EmployeeComponent,
  },
  {
    path: 'employees/new',
    component: EmployeeCreateComponent,
  },
  {
    path: 'employees/:id',
    component: EmployeeDetailComponent,
  },
];
