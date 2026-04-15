import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { departamentosAdminGuard } from './guards/departamentos-admin.guard';
import { DepartamentoCreatePage } from './pages/departamentos/departamento-create.page';
import { DepartamentoDetailPage } from './pages/departamentos/departamento-detail.page';
import { DepartamentoEditPage } from './pages/departamentos/departamento-edit.page';
import { DepartamentosListPage } from './pages/departamentos/departamentos-list.page';
import { EmpleadoCreatePage } from './pages/empleados/empleado-create.page';
import { EmpleadoDetailPage } from './pages/empleados/empleado-detail.page';
import { EmpleadoEditPage } from './pages/empleados/empleado-edit.page';
import { EmpleadosListPage } from './pages/empleados/empleados-list.page';
import { InicioPage } from './pages/inicio/inicio.page';
import { LoginPage } from './pages/login/login.page';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
  },
  {
    path: 'login',
    component: LoginPage,
  },
  {
    path: 'inicio',
    canActivate: [authGuard],
    component: InicioPage,
  },
  {
    path: 'empleados',
    canActivate: [authGuard],
    component: EmpleadosListPage,
  },
  {
    path: 'empleados/nuevo',
    canActivate: [authGuard, adminGuard],
    component: EmpleadoCreatePage,
  },
  {
    path: 'empleados/:clave',
    canActivate: [authGuard],
    component: EmpleadoDetailPage,
  },
  {
    path: 'empleados/:clave/editar',
    canActivate: [authGuard, adminGuard],
    component: EmpleadoEditPage,
  },
  {
    path: 'departamentos',
    canActivate: [authGuard],
    component: DepartamentosListPage,
  },
  {
    path: 'departamentos/nuevo',
    canActivate: [authGuard, departamentosAdminGuard],
    component: DepartamentoCreatePage,
  },
  {
    path: 'departamentos/:id',
    canActivate: [authGuard],
    component: DepartamentoDetailPage,
  },
  {
    path: 'departamentos/:id/editar',
    canActivate: [authGuard, departamentosAdminGuard],
    component: DepartamentoEditPage,
  },
  {
    path: '**',
    redirectTo: 'login',
  },
];
