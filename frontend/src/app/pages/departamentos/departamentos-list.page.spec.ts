import { ActivatedRoute, Router } from '@angular/router';
import { TestBed } from '@angular/core/testing';
import { signal } from '@angular/core';
import { of } from 'rxjs';
import { vi } from 'vitest';
import { AuthService } from '../../services/auth/auth.service';
import { PaginatedDepartamentosResponse } from '../../services/departamentos/departamentos.models';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';
import { DepartamentosListPage } from './departamentos-list.page';

describe('DepartamentosListPage', () => {
  const roleSignal = signal<'ADMIN' | 'EMPLEADO' | null>('EMPLEADO');

  const authMock = {
    role: roleSignal,
    isAuthenticated: vi.fn(() => true),
    isAdmin: vi.fn(() => false),
    logout: vi.fn(),
  };

  const departamentosServiceMock = {
    listar: vi.fn(() =>
      of<PaginatedDepartamentosResponse>({
        content: [],
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
      }),
    ),
    eliminar: vi.fn(() => of(void 0)),
    resolveActionMessage: vi.fn(() => 'No tienes permisos para esta acción.'),
  };

  const routerMock = {
    navigateByUrl: vi.fn(),
    navigate: vi.fn(),
  };

  const activatedRouteMock = {
    snapshot: {
      queryParamMap: {
        get: vi.fn((key: string) => (key === 'denied' ? '1' : null)),
      },
    },
  };

  beforeEach(() => {
    roleSignal.set('EMPLEADO');
    authMock.logout.mockReset();
    departamentosServiceMock.listar.mockClear();
    routerMock.navigateByUrl.mockReset();
    routerMock.navigate.mockReset();

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authMock },
        { provide: DepartamentosService, useValue: departamentosServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    });
  });

  it('muestra mensaje de permisos cuando llega con denied=1', () => {
    departamentosServiceMock.listar.mockReturnValueOnce(
      of<PaginatedDepartamentosResponse>({
        content: [{ id: 1, nombre: 'General', empleadosCount: 0 }],
        page: 0,
        size: 10,
        totalElements: 1,
        totalPages: 1,
      }),
    );

    const component = TestBed.createComponent(DepartamentosListPage).componentInstance;
    component.ngOnInit();

    expect(component.message()).toContain('No tienes permisos para esta acción.');
  });

  it('mantiene permisos de empleado tras recarga (instancia nueva)', () => {
    const first = TestBed.createComponent(DepartamentosListPage).componentInstance;
    first.ngOnInit();
    expect(first.permissions().puedeCrear).toBe(false);

    const second = TestBed.createComponent(DepartamentosListPage).componentInstance;
    second.ngOnInit();
    expect(second.permissions().puedeCrear).toBe(false);
  });

  it('bloquea navegación a crear para empleado', () => {
    const component = TestBed.createComponent(DepartamentosListPage).componentInstance;
    component.irANuevo();

    expect(component.message()).toContain('No tienes permisos para esta acción.');
    expect(routerMock.navigateByUrl).not.toHaveBeenCalled();
  });
});
