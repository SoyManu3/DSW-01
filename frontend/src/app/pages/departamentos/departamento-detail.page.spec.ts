import { ActivatedRoute, Router } from '@angular/router';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { vi } from 'vitest';
import { AuthService } from '../../services/auth/auth.service';
import { DepartamentosService } from '../../services/departamentos/departamentos.service';
import { DepartamentoDetailPage } from './departamento-detail.page';

describe('DepartamentoDetailPage', () => {
  const departamentosServiceMock = {
    obtenerPorId: vi.fn(() => of({ id: 1, nombre: 'General', empleadosCount: 2 })),
    resolveActionMessage: vi.fn(() => 'fallback'),
  };

  const authMock = {
    logout: vi.fn(),
  };

  const routerMock = {
    navigate: vi.fn(),
  };

  const activatedRouteMock = {
    snapshot: {
      paramMap: {
        get: vi.fn((key: string) => (key === 'id' ? '1' : null)),
      },
    },
  };

  beforeEach(() => {
    departamentosServiceMock.obtenerPorId.mockClear();

    TestBed.configureTestingModule({
      providers: [
        { provide: DepartamentosService, useValue: departamentosServiceMock },
        { provide: AuthService, useValue: authMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock },
      ],
    });
  });

  it('carga detalle de departamento en modo lectura', () => {
    const component = TestBed.createComponent(DepartamentoDetailPage).componentInstance;
    component.ngOnInit();

    expect(departamentosServiceMock.obtenerPorId).toHaveBeenCalledWith(1);
    expect(component.departamento()?.id).toBe(1);
  });
});
