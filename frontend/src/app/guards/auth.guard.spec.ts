import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { vi } from 'vitest';
import { AuthService } from '../services/auth/auth.service';
import { authGuard } from './auth.guard';

describe('authGuard', () => {
  const routerMock = {
    createUrlTree: vi.fn((value: unknown) => value),
  };

  const authMock = {
    isAuthenticated: vi.fn(),
  };

  beforeEach(() => {
    routerMock.createUrlTree.mockReset();
    authMock.isAuthenticated.mockReset();

    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: routerMock },
        { provide: AuthService, useValue: authMock },
      ],
    });
  });

  it('permite acceso cuando hay sesión válida', () => {
    authMock.isAuthenticated.mockReturnValue(true);

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(result).toBe(true);
  });

  it('redirecciona a login con señal de sesión inválida cuando no hay sesión', () => {
    authMock.isAuthenticated.mockReturnValue(false);

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(routerMock.createUrlTree).toHaveBeenCalledWith(['/login'], {
      queryParams: { reason: 'session-expired' },
    });
    expect(result).toEqual(['/login']);
  });
});
