export type EstadoAutenticacionUI =
  | 'INICIAL'
  | 'VALIDACION_ERROR'
  | 'ENVIANDO'
  | 'AUTENTICADO'
  | 'RECHAZADO'
  | 'ERROR_TECNICO';

export interface CredencialAccesoUI {
  emailCorporativo: string;
  contrasena: string;
}

export interface ResultadoLogin {
  resultado: 'ACEPTADO' | 'RECHAZADO' | 'FALLO_TECNICO';
  mensajeUsuario: string;
  rutaDestino?: '/inicio';
  rol?: RolAutenticado;
}

export type RolAutenticado = 'ADMIN' | 'EMPLEADO';

export interface LoginApiRequest {
  usuario: string;
  contraseña: string;
}

export interface LoginApiResponse {
  token: string;
  usuario: string;
  rol: RolAutenticado;
}
