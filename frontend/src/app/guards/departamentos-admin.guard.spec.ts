import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';
import { AuthService } from '../services/auth/auth.service';
import { departamentosAdminGuard } from './departamentos-admin.guard';

describe('departamentosAdminGuard', () => {
  const routerMock = {
    createUrlTree: vi.fn((value: unknown) => value),
  };

  const authMock = {
    isAuthenticated: vi.fn(),
    isAdmin: vi.fn(),
  };

  beforeEach(() => {
    routerMock.createUrlTree.mockReset();
    authMock.isAuthenticated.mockReset();
    authMock.isAdmin.mockReset();

    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: routerMock },
        { provide: AuthService, useValue: authMock },
      ],
    });
  });

  it('redirecciona a login con reason session-expired si no está autenticado', () => {
    authMock.isAuthenticated.mockReturnValue(false);

    const result = TestBed.runInInjectionContext(() =>
      departamentosAdminGuard({} as never, {} as never),
    );

    expect(routerMock.createUrlTree).toHaveBeenCalledWith(['/login'], {
      queryParams: { reason: 'session-expired' },
    });
    expect(result).toEqual(['/login']);
  });

  it('permite acceso si es admin', () => {
    authMock.isAuthenticated.mockReturnValue(true);
    authMock.isAdmin.mockReturnValue(true);

    const result = TestBed.runInInjectionContext(() =>
      departamentosAdminGuard({} as never, {} as never),
    );

    expect(result).toBe(true);
  });

  it('redirecciona a /departamentos con denied=1 si no es admin', () => {
    authMock.isAuthenticated.mockReturnValue(true);
    authMock.isAdmin.mockReturnValue(false);

    const result = TestBed.runInInjectionContext(() =>
      departamentosAdminGuard({} as never, {} as never),
    );

    expect(routerMock.createUrlTree).toHaveBeenCalledWith(['/departamentos'], {
      queryParams: { denied: '1' },
    });
    expect(result).toEqual(['/departamentos']);
  });
});
