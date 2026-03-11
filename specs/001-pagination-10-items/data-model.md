# Phase 1 - Data Model: Paginación de empleados

## Entity: Empleado

### Campos relevantes para consulta

- `clave` (String): `EMP-<n>`
- `nombre` (String)
- `direccion` (String)
- `telefono` (String)

## Value Object: PaginatedEmpleadosResponse

### Campos

- `content` (Array<Empleado>)
  - Colección de empleados de la página solicitada
  - Máximo 10 instancias por respuesta
- `page` (Integer)
  - Índice de página actual (base 0)
- `size` (Integer)
  - Valor fijo `10`
- `totalElements` (Long)
  - Total de empleados disponibles
- `totalPages` (Integer)
  - Total de páginas calculadas

## Reglas de validación

- Toda consulta de colección devuelve sobre paginado, no arreglo plano.
- `size` debe ser siempre `10`.
- Si `page` es negativa, se procesa como `0`.
- El orden de resultados para paginación es por `numero_autonumerico` ascendente.

## State Transitions

- No hay cambios de estado de dominio en esta feature; solo lectura paginada.
