# Phase 1 - Data Model: CRUD de Departamentos y Relación con Empleados

## Entity: Departamento

Representa una unidad organizativa a la que se asignan empleados.

### Campos

- `id` (Long)
  - Reglas:
    - Identificador único autogenerado.
    - Obligatorio en persistencia.
    - Inmutable una vez creado.
- `nombre` (String)
  - Reglas:
    - Obligatorio.
    - No puede ser nulo, vacío ni solo espacios.
    - Longitud máxima: 100 caracteres.
    - Único con comparación sensible a mayúsculas/minúsculas.

## Entity: Empleado (ampliación)

Entidad existente que ahora pertenece obligatoriamente a un departamento.

### Campos relevantes para esta feature

- `id` (EmpleadoId embebido)
- `clave` (derivada de `EmpleadoId`)
- `nombre` (String)
- `correo` (String)
- `departamento` (Departamento)
  - Reglas:
    - Obligatorio para todo empleado.
    - Referencia a un único departamento.

## Relaciones

- `Departamento 1 --- N Empleado`
  - Un departamento puede tener cero o muchos empleados.
  - Un empleado pertenece obligatoriamente a un solo departamento.

## Reglas de validación de dominio

- No se puede crear ni actualizar `Departamento` con `nombre` inválido (nulo, en blanco, >100).
- No se puede crear `Departamento` con `nombre` duplicado exacto (case-sensitive).
- No se puede eliminar un `Departamento` que tenga empleados asociados.
- No se puede crear ni actualizar `Empleado` sin `departamento` asociado.

## Reglas de consulta

- Listado de departamentos:
  - Paginado con tamaño fijo de 10.
  - Orden por defecto: `id` ascendente.
- Listado de empleados por departamento:
  - Paginado con tamaño fijo de 10.
  - Orden por defecto: `id` ascendente (sobre la porción numérica o criterio equivalente persistente).

## DTOs / Proyecciones relevantes

- `DepartamentoCreateRequest`
  - `nombre`
- `DepartamentoUpdateRequest`
  - `nombre`
- `DepartamentoResponse`
  - `id`, `nombre`, `empleadosCount`
- `PaginatedDepartamentosResponse`
  - `content`, `page`, `size`, `totalElements`, `totalPages`
- `EmpleadoDepartamentoResponse`
  - `clave`, `nombre`, `correo`, `departamentoId`
- `PaginatedEmpleadosDepartamentoResponse`
  - `content`, `page`, `size`, `totalElements`, `totalPages`

## Estados y transiciones

### Departamento

- `ACTIVE` (estado implícito): existente y usable.
- `DELETABLE`: `empleadosCount = 0`.
- `NON_DELETABLE`: `empleadosCount > 0`.

Transiciones:

- `ACTIVE + empleadosCount=0 --(delete)--> DELETED`
- `ACTIVE + empleadosCount>0 --(delete)--> NON_DELETABLE (rechazo)`

### Empleado (solo aspecto de departamento)

- `ASSIGNED`: empleado con departamento obligatorio.

Transiciones válidas:

- `ASSIGNED --(reasignar departamento)--> ASSIGNED`

Transiciones inválidas:

- `ASSIGNED --(quitar departamento)--> INVALID`
