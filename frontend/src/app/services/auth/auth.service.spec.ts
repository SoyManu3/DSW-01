import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
  });

  it('inicia sin sesión si no hay datos en storage', () => {
    const service = TestBed.inject(AuthService);
    expect(service.isAuthenticated()).toBe(false);
    expect(service.role()).toBeNull();
  });

  it('descarta sesión si hay auth header pero falta rol', () => {
    localStorage.setItem('empleados_basic_auth_header', 'Basic xxx');

    const service = TestBed.inject(AuthService);

    expect(service.isAuthenticated()).toBe(false);
    expect(service.role()).toBeNull();
    expect(localStorage.getItem('empleados_basic_auth_header')).toBeNull();
  });

  it('limpia auth y rol en logout', () => {
    localStorage.setItem('empleados_basic_auth_header', 'Basic xxx');
    localStorage.setItem('empleados_auth_role', 'ADMIN');

    const service = TestBed.inject(AuthService);
    service.logout();

    expect(localStorage.getItem('empleados_basic_auth_header')).toBeNull();
    expect(localStorage.getItem('empleados_auth_role')).toBeNull();
    expect(service.isAuthenticated()).toBe(false);
    expect(service.role()).toBeNull();
  });
});
