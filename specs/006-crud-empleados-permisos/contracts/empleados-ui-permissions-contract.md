# Contrato UI: Permisos CRUD de Empleados por Rol

## 1. Objetivo

Definir el comportamiento observable de la interfaz de empleados según rol autenticado (`ADMIN` o `EMPLEADO`) usando la sesión y rol proporcionados por backend.

## 2. Fuente de rol

- La UI obtiene el rol desde la respuesta de login (`rol`).
- Valores válidos: `ADMIN`, `EMPLEADO`.
- Si el rol no está presente o es inválido, la sesión se considera no autorizada para escritura y opera como lectura restringida.

## 3. Matriz de permisos en interfaz

| Acción UI                | ADMIN     | EMPLEADO     |
| ------------------------ | --------- | ------------ |
| Ver listado de empleados | Permitido | Permitido    |
| Ver detalle de empleado  | Permitido | Permitido    |
| Crear empleado           | Permitido | No permitido |
| Editar empleado          | Permitido | No permitido |
| Eliminar empleado        | Permitido | No permitido |

## 4. Reglas de navegación

- Rutas de lectura (`/empleados`) requieren autenticación.
- Rutas de escritura (`/empleados/nuevo`, `/empleados/:clave/editar`) requieren rol `ADMIN`.
- Si usuario `EMPLEADO` accede por URL a rutas de escritura:
  - redirección a `/empleados`
  - mensaje visible: permisos insuficientes

## 5. Contrato de integración API consumido por UI

- `POST /api/v1/auth/login`
  - Respuesta exitosa incluye `rol`.
- `GET /api/v1/empleados?page={n}`
  - Acceso autenticado para ambos roles.
  - Respuesta paginada con `size=10`.
- `POST /api/v1/empleados`
- `PUT /api/v1/empleados/{clave}`
- `DELETE /api/v1/empleados/{clave}`
  - UI solo debe invocar estas operaciones en contexto `ADMIN`.
  - Si backend responde autorización denegada, UI muestra mensaje de permisos insuficientes y mantiene consistencia de pantalla.

## 6. Criterios verificables

1. En sesión `ADMIN`, controles CRUD visibles y operativos.
2. En sesión `EMPLEADO`, controles CRUD ausentes o deshabilitados sin posibilidad de operación efectiva.
3. Acceso directo por URL de escritura en sesión `EMPLEADO` redirige a listado en modo lectura.
4. Cualquier denegación de backend en operaciones de escritura se refleja con feedback de permisos y sin corrupción de estado visual.
