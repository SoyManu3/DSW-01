import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  DepartamentoCreatePayload,
  DepartamentoUpdatePayload,
  DepartamentoView,
  PaginatedDepartamentosResponse,
} from './departamentos.models';

@Injectable({
  providedIn: 'root',
})
export class DepartamentosService {
  private readonly http = inject(HttpClient);
  private readonly api = `${environment.apiBaseUrl}/api/v1/departamentos`;

  listar(page = 0): Observable<PaginatedDepartamentosResponse> {
    return this.http.get<PaginatedDepartamentosResponse>(`${this.api}?page=${page}`);
  }

  obtenerPorId(id: number): Observable<DepartamentoView> {
    return this.http.get<DepartamentoView>(`${this.api}/${id}`);
  }

  crear(payload: DepartamentoCreatePayload): Observable<DepartamentoView> {
    return this.http.post<DepartamentoView>(this.api, payload);
  }

  actualizar(id: number, payload: DepartamentoUpdatePayload): Observable<DepartamentoView> {
    return this.http.put<DepartamentoView>(`${this.api}/${id}`, payload);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  resolveActionMessage(error: unknown, fallback: string): string {
    if (error instanceof HttpErrorResponse && error.status === 401) {
      return 'Sesión expirada o no autenticada.';
    }

    if (error instanceof HttpErrorResponse && error.status === 403) {
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
