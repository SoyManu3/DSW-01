import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { DepartamentosService } from './departamentos.service';

describe('DepartamentosService', () => {
  let service: DepartamentosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(DepartamentosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lista departamentos paginados con size 10', () => {
    service.listar(0).subscribe((response) => {
      expect(response.page).toBe(0);
      expect(response.size).toBe(10);
      expect(response.content.length).toBe(1);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/departamentos?page=0');
    expect(req.request.method).toBe('GET');
    req.flush({
      content: [{ id: 1, nombre: 'General', empleadosCount: 2 }],
      page: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
    });
  });

  it('crea departamento', () => {
    service.crear({ nombre: 'Ventas' }).subscribe((departamento) => {
      expect(departamento.nombre).toBe('Ventas');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/departamentos');
    expect(req.request.method).toBe('POST');
    req.flush({ id: 10, nombre: 'Ventas', empleadosCount: 0 });
  });

  it('actualiza departamento', () => {
    service.actualizar(10, { nombre: 'Comercial' }).subscribe((departamento) => {
      expect(departamento.nombre).toBe('Comercial');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/departamentos/10');
    expect(req.request.method).toBe('PUT');
    req.flush({ id: 10, nombre: 'Comercial', empleadosCount: 0 });
  });

  it('elimina departamento', () => {
    service.eliminar(10).subscribe((response) => expect(response).toBeUndefined());

    const req = httpMock.expectOne('http://localhost:8080/api/v1/departamentos/10');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('resuelve mensajes por estado de error', () => {
    const status401Message = service.resolveActionMessage(
      new HttpErrorResponse({ status: 401 }),
      'fallback',
    );
    const status403Message = service.resolveActionMessage(
      new HttpErrorResponse({ status: 403 }),
      'fallback',
    );

    expect(status401Message).toBe('Sesión expirada o no autenticada.');
    expect(status403Message).toBe('No tienes permisos para esta acción.');
  });
});
