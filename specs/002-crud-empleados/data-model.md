# Phase 1 - Data Model: CRUD de empleados

## Entity: Empleado

### Campos

- `prefijo` (String, PK compuesto)
  - Reglas:
    - Valor fijo `EMP`
    - Obligatorio
    - Inmutable después de creación
- `numero_autonumerico` (Integer, PK compuesto)
  - Reglas:
    - Entero positivo autoincremental
    - Obligatorio
    - Inmutable después de creación
- `clave` (String, derivado para interfaz externa)
  - Reglas:
    - Formato `EMP-<numero_autonumerico>`
    - Generado por sistema
    - Único e inmutable
- `nombre` (String, max 100)
  - Reglas:
    - Obligatorio
    - Longitud 1..100
- `direccion` (String, max 100)
  - Reglas:
    - Obligatorio
    - Longitud 1..100
- `telefono` (String, max 100)
  - Reglas:
    - Obligatorio
    - Longitud 1..100

## Relaciones

- No hay relaciones obligatorias con otras entidades en esta feature.

## Value Object: PaginaEmpleados

### Campos

- `content` (Array<Empleado>)
  - Reglas:
    - Colección de empleados de la página solicitada
    - Máximo 10 instancias por respuesta
- `page` (Integer)
  - Reglas:
    - Índice base 0 de la página actual
- `size` (Integer)
  - Reglas:
    - Valor fijo `10`
- `totalElements` (Long)
  - Reglas:
    - Total de empleados existentes
- `totalPages` (Integer)
  - Reglas:
    - Cantidad total de páginas disponibles

## Reglas de validación

- Rechazar creación manual con `clave` en payload de alta.
- Garantizar unicidad de `numero_autonumerico` dentro del prefijo `EMP`.
- Rechazar payload con campos vacíos o con solo espacios.
- Rechazar cualquier `nombre`, `direccion` o `telefono` con longitud > 100.
- Rechazar intento de cambio de `clave` en actualización.
- Rechazar intento de cambio de `prefijo` o `numero_autonumerico` en actualización.
- En consultas de colección, devolver siempre tamaño de página 10.

## Estado y transiciones

- `CREATED`: empleado persistido tras alta exitosa.
- `UPDATED`: empleado existente actualizado en campos permitidos.
- `DELETED`: empleado eliminado por clave.

Transiciones válidas:

- `CREATE -> CREATED`
- `CREATED -> UPDATED`
- `CREATED/UPDATED -> DELETED`

Transiciones inválidas:

- `DELETED -> UPDATED`
- `CREATE` con clave manual enviada por cliente.
