import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  EmpleadoCreatePayload,
  EmpleadoUpdatePayload,
  EmpleadoView,
  PaginatedEmpleadosResponse,
} from './empleados.models';

@Injectable({
  providedIn: 'root',
})
export class EmpleadosService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiBaseUrl}/api/v1/empleados`;

  listar(page = 0): Observable<PaginatedEmpleadosResponse> {
    return this.http.get<PaginatedEmpleadosResponse>(`${this.api}?page=${page}`);
  }

  obtenerPorClave(clave: string): Observable<EmpleadoView> {
    return this.http.get<EmpleadoView>(`${this.api}/${clave}`);
  }

  crear(payload: EmpleadoCreatePayload): Observable<EmpleadoView> {
    return this.http.post<EmpleadoView>(this.api, payload);
  }

  actualizar(clave: string, payload: EmpleadoUpdatePayload): Observable<EmpleadoView> {
    return this.http.put<EmpleadoView>(`${this.api}/${clave}`, payload);
  }

  eliminar(clave: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${clave}`);
  }

  resolveActionMessage(error: unknown, fallback: string): string {
    if (error instanceof HttpErrorResponse && (error.status === 401 || error.status === 403)) {
      return 'No tienes permisos para esta acción.';
    }

    if (
      error instanceof HttpErrorResponse &&
      typeof error.error?.message === 'string' &&
      error.error.message.trim()
    ) {
      return error.error.message;
    }

    return fallback;
  }
}
