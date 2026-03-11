# Feature Specification: Paginación de consultas de empleados

**Feature Branch**: `001-pagination-10-items`  
**Created**: 2026-03-04  
**Status**: Draft  
**Input**: User description: "Aplicar paginación de 10 en 10 en las consultas en el API de empleados"

## Clarifications

### Session 2026-03-04

- Q: ¿Qué formato debe tener la respuesta paginada del listado? → A: Respuesta con metadatos `content`, `page`, `size`, `totalElements`, `totalPages` y tamaño fijo de 10.
- Q: ¿Qué base de ruta debe usarse para esta versión? → A: `/api/v1/empleados`.

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Listar empleados paginados (Priority: P1)

Como usuario administrativo, quiero consultar empleados en páginas de 10 elementos para navegar el padrón sin respuestas masivas.

**Why this priority**: es el objetivo principal de la feature y habilita consumo eficiente del listado.

**Independent Test**: invocar `GET /api/v1/empleados?page=0` y validar sobre paginado, `size=10` y cantidad de elementos `<=10`.

**Acceptance Scenarios**:

1. **Given** que existen más de 10 empleados, **When** consulto `GET /api/v1/empleados?page=0`, **Then** recibo `200` con `size=10`, `page=0`, `content` de hasta 10 elementos y metadatos totales.
2. **Given** que existen más de 10 empleados, **When** consulto `GET /api/v1/empleados?page=1`, **Then** recibo la siguiente página con hasta 10 elementos.
3. **Given** que la última página contiene menos de 10 empleados, **When** la consulto, **Then** `content` devuelve solo los elementos restantes.

---

### User Story 2 - Compatibilidad de seguridad y contrato (Priority: P2)

Como consumidor interno del API, quiero que la consulta paginada respete Basic Auth y esté documentada en OpenAPI para integrarme sin ambigüedad.

**Why this priority**: evita regresiones de seguridad y garantiza trazabilidad contractual.

**Independent Test**: acceder sin credenciales debe devolver `401`; con credenciales válidas debe devolver `200` y esquema paginado documentado en OpenAPI.

**Acceptance Scenarios**:

1. **Given** que no envío credenciales, **When** consulto `GET /api/v1/empleados?page=0`, **Then** recibo `401`.
2. **Given** que envío credenciales válidas, **When** consulto `GET /api/v1/empleados?page=0`, **Then** recibo `200` con el contrato paginado definido.

### Edge Cases

- Si `page` es negativa, el sistema la normaliza a `0`.
- Si no existen empleados, el sistema devuelve `content=[]`, `totalElements=0` y `totalPages=0`.
- Si `page` excede el total de páginas, el sistema devuelve `content=[]` manteniendo metadatos coherentes.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST exponer el listado de empleados en `GET /api/v1/empleados`.
- **FR-002**: El sistema MUST aceptar el parámetro de consulta `page` (base 0).
- **FR-003**: El sistema MUST devolver consultas de colección en un sobre paginado con campos `content`, `page`, `size`, `totalElements` y `totalPages`.
- **FR-004**: El sistema MUST aplicar tamaño fijo de página `10` en todas las consultas del listado.
- **FR-005**: El sistema MUST devolver `content` con un máximo de 10 elementos por respuesta.
- **FR-006**: El sistema MUST mantener autenticación Basic Auth en el endpoint paginado.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La feature MUST mantener implementación sobre Java 17 y Spring Boot 3.
- **CA-002**: La feature MUST mantener protección de acceso mediante HTTP Basic Auth.
- **CA-003**: La feature MUST mantener persistencia en PostgreSQL y entorno local con Docker Compose.
- **CA-004**: La feature MUST actualizar contrato OpenAPI/Swagger para la consulta paginada.
- **CA-005**: La feature MUST usar rutas versionadas `/api/v1/...`.
- **CA-006**: La feature MUST garantizar paginación determinista de 10 elementos por consulta.

### Key Entities _(include if feature involves data)_

- **Empleado**: entidad existente con `clave`, `nombre`, `direccion`, `telefono`.
- **PaginaEmpleados**: value object de respuesta con `content`, `page`, `size`, `totalElements`, `totalPages`.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: El 100% de respuestas de `GET /api/v1/empleados` incluye los 5 campos de metadatos de paginación.
- **SC-002**: El 100% de respuestas de listado devuelve `size = 10`.
- **SC-003**: El 100% de respuestas del listado devuelve `content` con `<= 10` empleados.
- **SC-004**: El 100% de accesos sin credenciales válidas al listado responde `401`.
