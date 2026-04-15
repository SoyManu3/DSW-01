import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, catchError, map, of } from 'rxjs';
import { environment } from '../../../environments/environment';
import { encodeBasicAuth } from './basic-auth.encoder';
import { mapAuthError } from './auth-error.mapper';
import {
  CredencialAccesoUI,
  LoginApiRequest,
  LoginApiResponse,
  ResultadoLogin,
  RolAutenticado,
} from './auth.models';

const AUTH_STORAGE_KEY = 'empleados_basic_auth_header';
const AUTH_ROLE_STORAGE_KEY = 'empleados_auth_role';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly initialRole = this.getStoredRole();
  private readonly authenticatedState = signal<boolean>(this.hasStoredSession());
  private readonly roleState = signal<RolAutenticado | null>(this.initialRole);

  readonly isAuthenticated = computed(() => this.authenticatedState());
  readonly role = computed(() => this.roleState());
  readonly isAdmin = computed(() => this.roleState() === 'ADMIN');

  constructor() {
    if (!this.initialRole && localStorage.getItem(AUTH_STORAGE_KEY)) {
      this.logout();
    }
  }

  login(credentials: CredencialAccesoUI): Observable<ResultadoLogin> {
    const authorization = encodeBasicAuth(credentials.emailCorporativo, credentials.contrasena);
    const request: LoginApiRequest = {
      usuario: credentials.emailCorporativo,
      contraseña: credentials.contrasena,
    };

    return this.http
      .post<LoginApiResponse>(`${environment.apiBaseUrl}/api/v1/auth/login`, request, {
        headers: new HttpHeaders({ Authorization: authorization }),
      })
      .pipe(
        map((response) => {
          this.persistSession(authorization, response.rol);
          return {
            resultado: 'ACEPTADO',
            mensajeUsuario: '',
            rutaDestino: '/inicio',
            rol: response.rol,
          } as ResultadoLogin;
        }),
        catchError((error) => of(mapAuthError(error))),
      );
  }

  logout(): void {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    localStorage.removeItem(AUTH_ROLE_STORAGE_KEY);
    this.authenticatedState.set(false);
    this.roleState.set(null);
  }

  getAuthorizationHeader(): string | null {
    return localStorage.getItem(AUTH_STORAGE_KEY);
  }

  private persistSession(authorization: string, rol: RolAutenticado): void {
    localStorage.setItem(AUTH_STORAGE_KEY, authorization);
    localStorage.setItem(AUTH_ROLE_STORAGE_KEY, rol);
    this.authenticatedState.set(true);
    this.roleState.set(rol);
  }

  private hasStoredSession(): boolean {
    return !!localStorage.getItem(AUTH_STORAGE_KEY) && this.initialRole !== null;
  }

  private getStoredRole(): RolAutenticado | null {
    const storedRole = localStorage.getItem(AUTH_ROLE_STORAGE_KEY);
    if (storedRole === 'ADMIN' || storedRole === 'EMPLEADO') {
      return storedRole;
    }
    return null;
  }
}
