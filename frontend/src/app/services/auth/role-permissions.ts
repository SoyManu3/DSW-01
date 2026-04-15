import { RolAutenticado } from './auth.models';

export interface UiRolePermissions {
  puedeCrear: boolean;
  puedeEditar: boolean;
  puedeEliminar: boolean;
  modoLectura: boolean;
}

const ADMIN_PERMISSIONS: UiRolePermissions = {
  puedeCrear: true,
  puedeEditar: true,
  puedeEliminar: true,
  modoLectura: false,
};

const EMPLEADO_PERMISSIONS: UiRolePermissions = {
  puedeCrear: false,
  puedeEditar: false,
  puedeEliminar: false,
  modoLectura: true,
};

export function resolvePermissionsByRole(rol: RolAutenticado | null): UiRolePermissions {
  return rol === 'ADMIN' ? ADMIN_PERMISSIONS : EMPLEADO_PERMISSIONS;
}
