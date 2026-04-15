# Data Model: CRUD visual de empleados con permisos por rol

## 1. SesionAutenticadaUI

- **Descripción**: Estado local de sesión en frontend usado para autorización visual.
- **Campos**:
  - `authorizationHeader` (string, obligatorio cuando autenticado)
  - `rol` (`ADMIN` | `EMPLEADO`, obligatorio cuando autenticado)
  - `isAuthenticated` (boolean)
- **Reglas de validación**:
  - Si `isAuthenticated=true`, `authorizationHeader` y `rol` deben existir.
  - `rol` solo acepta `ADMIN` o `EMPLEADO`.

## 2. EmpleadoView

- **Descripción**: Modelo de lectura para renderizar filas/tabla/lista de empleados.
- **Campos**:
  - `clave` (string, patrón `EMP-<n>`)
  - `nombre` (string)
  - `direccion` (string)
  - `telefono` (string)
  - `correo` (string)
  - `activo` (boolean)
- **Relaciones**:
  - Proviene de `PaginatedEmpleadosResponse.content` del backend.

## 3. PermisoAccionUI

- **Descripción**: Estado derivado que gobierna acciones del CRUD en interfaz.
- **Campos derivados**:
  - `puedeCrear` (boolean)
  - `puedeEditar` (boolean)
  - `puedeEliminar` (boolean)
  - `modoLectura` (boolean)
- **Reglas**:
  - Si `rol=ADMIN` → `puedeCrear=true`, `puedeEditar=true`, `puedeEliminar=true`, `modoLectura=false`.
  - Si `rol=EMPLEADO` → `puedeCrear=false`, `puedeEditar=false`, `puedeEliminar=false`, `modoLectura=true`.

## 4. EstadoPaginaEmpleadosUI

- **Descripción**: Estado de interacción de la pantalla CRUD/listado.
- **Estados permitidos**:
  - `CARGANDO_LISTA`
  - `LISTA_OK`
  - `LISTA_VACIA`
  - `OPERACION_EXITOSA`
  - `PERMISO_DENEGADO`
  - `ERROR_TECNICO`
- **Transiciones clave**:
  - `CARGANDO_LISTA -> LISTA_OK` (respuesta con elementos)
  - `CARGANDO_LISTA -> LISTA_VACIA` (respuesta sin elementos)
  - `LISTA_OK -> OPERACION_EXITOSA` (alta/edición/eliminación admin OK)
  - `LISTA_OK -> PERMISO_DENEGADO` (acción no permitida por rol o 401/403)
  - `* -> ERROR_TECNICO` (fallo de red/servidor no autorizado no aplicable)

## 5. RutasProtegidasEmpleados

- **Descripción**: Reglas de acceso para navegación.
- **Rutas esperadas**:
  - `/empleados` (acceso autenticado: admin y empleado)
  - `/empleados/nuevo` (solo admin)
  - `/empleados/:clave/editar` (solo admin)
- **Reglas de transición**:
  - Usuario no autenticado: redirección a `/login`.
  - Usuario empleado en rutas de escritura: redirección a `/empleados` con mensaje de permisos insuficientes.
