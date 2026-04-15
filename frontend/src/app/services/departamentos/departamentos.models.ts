export interface DepartamentoView {
  id: number;
  nombre: string;
  empleadosCount: number;
}

export interface PaginatedDepartamentosResponse {
  content: DepartamentoView[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface DepartamentoCreatePayload {
  nombre: string;
}

export interface DepartamentoUpdatePayload {
  nombre: string;
}
