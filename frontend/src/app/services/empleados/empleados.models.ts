export interface EmpleadoView {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  activo: boolean;
}

export interface PaginatedEmpleadosResponse {
  content: EmpleadoView[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface EmpleadoCreatePayload {
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  password: string;
  departamentoId: number;
}

export interface EmpleadoUpdatePayload {
  nombre: string;
  direccion: string;
  telefono: string;
  correo: string;
  password?: string;
  departamentoId: number;
}
