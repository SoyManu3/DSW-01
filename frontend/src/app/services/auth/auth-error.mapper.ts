import { HttpErrorResponse } from '@angular/common/http';
import { ResultadoLogin } from './auth.models';

export function mapAuthError(error: HttpErrorResponse): ResultadoLogin {
  if (error.status === 401 || error.status === 403) {
    return {
      resultado: 'RECHAZADO',
      mensajeUsuario: 'Credenciales inválidas',
    };
  }

  return {
    resultado: 'FALLO_TECNICO',
    mensajeUsuario: 'No fue posible validar tus credenciales. Intenta nuevamente.',
  };
}
