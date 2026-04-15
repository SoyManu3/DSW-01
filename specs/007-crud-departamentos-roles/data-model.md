# Data Model: CRUD visual de departamentos con permisos por rol

## 1. SesionAutenticadaUI

- **Descripción**: Estado de autenticación en frontend utilizado para autorización visual y navegación protegida.
- **Campos**:
  - `authorizationHeader` (string, obligatorio cuando autenticado)
  - `rol` (`ADMIN` | `EMPLEADO`, obligatorio cuando autenticado)
  - `isAuthenticated` (boolean)
- **Reglas de validación**:
  - Si `isAuthenticated=true`, `authorizationHeader` y `rol` deben existir.
  - `rol` solo permite valores `ADMIN` o `EMPLEADO`.

## 2. DepartamentoView

- **Descripción**: Modelo de lectura para renderizar filas del listado de departamentos.
- **Campos**:
  - `id` (number)
  - `nombre` (string)
  - `empleadosCount` (number)
- **Relaciones**:
  - Proviene de `PaginatedDepartamentosResponse.content` del backend.

## 3. PermisoAccionDepartamentosUI

- **Descripción**: Estado derivado que controla acciones habilitadas en el módulo de departamentos.
- **Campos derivados**:
  - `puedeCrear` (boolean)
  - `puedeEditar` (boolean)
  - `puedeEliminar` (boolean)
  - `modoLectura` (boolean)
- **Reglas**:
  - Si `rol=ADMIN` → `puedeCrear=true`, `puedeEditar=true`, `puedeEliminar=true`, `modoLectura=false`.
  - Si `rol=EMPLEADO` → `puedeCrear=false`, `puedeEditar=false`, `puedeEliminar=false`, `modoLectura=true`.
  - En `rol=EMPLEADO`, los controles CRUD son visibles y deshabilitados.

## 4. EstadoPaginaDepartamentosUI

- **Descripción**: Estados de interacción del módulo visual de departamentos.
- **Estados permitidos**:
  - `CARGANDO_LISTA`
  - `LISTA_OK`
  - `LISTA_VACIA`
  - `OPERACION_EXITOSA`
  - `PERMISO_DENEGADO`
  - `SESION_INVALIDA`
  - `ERROR_TECNICO`
- **Transiciones clave**:
  - `CARGANDO_LISTA -> LISTA_OK` (respuesta con elementos)
  - `CARGANDO_LISTA -> LISTA_VACIA` (respuesta sin elementos)
  - `LISTA_OK -> OPERACION_EXITOSA` (create/update/delete exitoso en admin)
  - `LISTA_OK -> PERMISO_DENEGADO` (acción restringida o 403)
  - `* -> SESION_INVALIDA` (401 o ausencia de sesión)
  - `* -> ERROR_TECNICO` (fallos no relacionados con permisos)

## 5. RutasProtegidasDepartamentos

- **Descripción**: Reglas de acceso por rol para navegación en Angular Router.
- **Rutas esperadas**:
  - `/departamentos` (usuarios autenticados: `ADMIN` y `EMPLEADO`)
  - `/departamentos/nuevo` (solo `ADMIN`)
  - `/departamentos/:id/editar` (solo `ADMIN`)
  - `/departamentos/:id` (usuarios autenticados: `ADMIN` y `EMPLEADO`, detalle lectura)
- **Reglas de transición**:
  - No autenticado o sesión expirada: redirección a `/login` con `Sesión expirada o no autenticada.`.
  - `EMPLEADO` en rutas de escritura: redirección a `/departamentos` con `No tienes permisos para esta acción.`.

## 6. Contratos de error relevantes para UI

- **`UnauthorizedErrorUI`**:
  - `httpStatus` = 401
  - `mensajeUsuario` = `Sesión expirada o no autenticada.`
  - `accionUI` = redirección a `/login`
- **`ForbiddenErrorUI`**:
  - `httpStatus` = 403
  - `mensajeUsuario` = `No tienes permisos para esta acción.`
  - `accionUI` = mantener o redirigir a listado en modo lectura según origen de navegación
