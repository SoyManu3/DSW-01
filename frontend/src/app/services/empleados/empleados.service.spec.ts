import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { EmpleadosService } from './empleados.service';

describe('EmpleadosService', () => {
  let service: EmpleadosService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(EmpleadosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('lista empleados paginados', () => {
    service.listar(0).subscribe((response) => {
      expect(response.size).toBe(10);
      expect(response.content.length).toBe(1);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/empleados?page=0');
    expect(req.request.method).toBe('GET');
    req.flush({
      content: [
        {
          clave: 'EMP-1',
          nombre: 'Admin',
          direccion: 'Dir',
          telefono: '111',
          correo: 'admin@test.com',
          activo: true,
        },
      ],
      page: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
    });
  });

  it('crea empleado', () => {
    service
      .crear({
        nombre: 'Ana',
        direccion: 'Av 1',
        telefono: '333',
        correo: 'ana@test.com',
        password: 'clave123',
        departamentoId: 1,
      })
      .subscribe((empleado) => expect(empleado.clave).toBe('EMP-2'));

    const req = httpMock.expectOne('http://localhost:8080/api/v1/empleados');
    expect(req.request.method).toBe('POST');
    req.flush({
      clave: 'EMP-2',
      nombre: 'Ana',
      direccion: 'Av 1',
      telefono: '333',
      correo: 'ana@test.com',
      activo: true,
    });
  });

  it('actualiza empleado', () => {
    service
      .actualizar('EMP-1', {
        nombre: 'Admin',
        direccion: 'Nueva',
        telefono: '111',
        correo: 'admin@test.com',
        departamentoId: 1,
      })
      .subscribe((empleado) => expect(empleado.direccion).toBe('Nueva'));

    const req = httpMock.expectOne('http://localhost:8080/api/v1/empleados/EMP-1');
    expect(req.request.method).toBe('PUT');
    req.flush({
      clave: 'EMP-1',
      nombre: 'Admin',
      direccion: 'Nueva',
      telefono: '111',
      correo: 'admin@test.com',
      activo: true,
    });
  });

  it('elimina empleado', () => {
    service.eliminar('EMP-3').subscribe((response) => expect(response).toBeUndefined());

    const req = httpMock.expectOne('http://localhost:8080/api/v1/empleados/EMP-3');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
