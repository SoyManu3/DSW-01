# UI Permissions Contract: Departamentos

## Objetivo

Definir el comportamiento observable de la UI de departamentos según rol autenticado y estado de sesión.

## Roles soportados

- `ADMIN`
- `EMPLEADO`

## Matriz de capacidades

| Acción UI                          | ADMIN     | EMPLEADO                        |
| ---------------------------------- | --------- | ------------------------------- |
| Ver listado `/departamentos`       | Permitido | Permitido                       |
| Ver detalle `/departamentos/:id`   | Permitido | Permitido                       |
| Crear `/departamentos/nuevo`       | Permitido | Denegado (redirigir a listado)  |
| Editar `/departamentos/:id/editar` | Permitido | Denegado (redirigir a listado)  |
| Eliminar desde listado/detalle     | Permitido | Denegado (acción deshabilitada) |

## Reglas de representación visual

1. Para `EMPLEADO`, los controles de crear/editar/eliminar se muestran en estado deshabilitado.
2. Para `ADMIN`, los controles de crear/editar/eliminar están habilitados.
3. La UI no debe habilitar transiciones de escritura para `EMPLEADO` aunque la ruta se invoque manualmente.

## Reglas de navegación

1. Usuario sin sesión válida en cualquier ruta protegida de departamentos:
   - Redirección a `/login`.
   - Mensaje: `Sesión expirada o no autenticada.`
2. Usuario `EMPLEADO` en ruta de escritura (`/departamentos/nuevo`, `/departamentos/:id/editar`):
   - Redirección a `/departamentos`.
   - Mensaje: `No tienes permisos para esta acción.`

## Reglas de mensajería

- Mensaje global para autorización denegada (403 o bloqueo de acción restringida):
  - `No tienes permisos para esta acción.`
- Mensaje global para autenticación ausente o expirada (401 o sesión inexistente):
  - `Sesión expirada o no autenticada.`

## Integración backend esperada

- Fuente de rol: respuesta de login `/api/v1/auth/login`.
- Endpoints de departamentos consumidos por UI:
  - `GET /api/v1/departamentos?page={n}`
  - `GET /api/v1/departamentos/{id}`
  - `POST /api/v1/departamentos`
  - `PUT /api/v1/departamentos/{id}`
  - `DELETE /api/v1/departamentos/{id}`
- Seguridad transportada por `Authorization: Basic ...`.

## Invariantes verificables

1. Ningún usuario `EMPLEADO` completa acciones de escritura de departamentos exitosamente.
2. Todo acceso sin sesión válida a rutas protegidas termina en `/login`.
3. Todo intento de escritura denegado comunica mensaje consistente de permisos.
