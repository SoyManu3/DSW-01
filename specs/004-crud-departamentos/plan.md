# Implementation Plan: CRUD de Departamentos y Relación con Empleados

**Branch**: `004-crud-departamentos` | **Date**: 2026-03-12 | **Spec**: /specs/004-crud-departamentos/spec.md
**Input**: Feature specification from `/specs/004-crud-departamentos/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar una nueva entidad `Departamento` con atributos `id` y `nombre` y CRUD
completo bajo `/api/v1/departamentos`, más la relación 1:N con `Empleado` donde cada
empleado pertenece obligatoriamente a un único departamento. El diseño incluirá
restricciones de nombre (no vacío, máximo 100 y unicidad sensible a mayúsculas),
listados paginados de 10 elementos con orden por `id` ascendente, endpoint dedicado
para listar empleados por departamento, reglas de borrado con protección de integridad,
migraciones Flyway, contrato OpenAPI/Swagger actualizado y pruebas unitarias + integración.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.4.x (Web, Security, Validation, Data JPA), Flyway, PostgreSQL driver, springdoc-openapi  
**Storage**: PostgreSQL (con Docker Compose en local/dev)  
**Testing**: JUnit 5, Spring Boot Test, Spring Security Test, Testcontainers PostgreSQL  
**Target Platform**: Linux server (API REST backend)
**Project Type**: web-service backend REST  
**Performance Goals**: p95 < 2s para operaciones CRUD y consultas paginadas en entorno de validación local  
**Constraints**: HTTP Basic Auth obligatorio, rutas versionadas `/api/v1/...`, tamaño de página fijo 10, orden por `id` ascendente, `nombre` de departamento <=100, unicidad case-sensitive, no permitir borrar departamento con empleados asociados, `departamento_id` obligatorio en empleados  
**Scale/Scope**: un módulo backend único; una nueva tabla (`departamentos`), alteración de `empleados` para FK, 5 endpoints de CRUD + 1 endpoint de empleados por departamento

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

- Estado pre-Phase 0: PASS

- Java runtime is pinned to Java 17 and backend framework is Spring Boot 3. (PASS)
- Authentication approach is explicitly defined as HTTP Basic Auth (or a documented
  exception approved in the plan). (PASS)
- Persistent storage targets PostgreSQL and local/dev execution includes Docker
  (e.g., Docker Compose) for database provisioning. (PASS)
- API contract and endpoint documentation plan includes OpenAPI/Swagger publication. (PASS)
- Public API routes include explicit versioning in path (e.g., `/api/v1/...`). (PASS)
- Collection query design enforces paginated responses with 10 instances per request. (PASS)
- Test strategy covers unit + integration tests for changed behavior and security-critical
  flows. (PASS)

## Project Structure

### Documentation (this feature)

```text
specs/004-crud-departamentos/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
src/main/java/com/example/empleados/
├── config/
├── controller/
│   └── dto/
├── domain/
├── repository/
└── service/

src/main/resources/
├── application.yml
└── db/migration/

src/test/java/com/example/empleados/
├── unit/
└── integration/

docker/
└── docker-compose.yml
```

**Structure Decision**: Se mantiene la arquitectura monolítica Spring Boot existente por
capas. La feature se implementa ampliando dominio, repositorios, servicios, controlador,
DTOs y migraciones Flyway dentro del mismo módulo `empleados`, y extendiendo pruebas
unitarias e integración en las carpetas ya establecidas.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
| --------- | ---------- | ------------------------------------ |
| Ninguna   | N/A        | N/A                                  |

## Post-Design Constitution Check

Estado post-Phase 1: PASS

- El modelo de datos añade `departamentos` y la FK hacia `empleados` manteniendo Java 17 + Spring Boot 3.
- La seguridad se conserva con HTTP Basic Auth para todos los endpoints nuevos bajo `/api/v1/departamentos/**`.
- Persistencia se mantiene en PostgreSQL con ejecución local reproducible por Docker Compose y migraciones Flyway.
- El contrato OpenAPI incluye CRUD de departamentos y listado paginado de empleados por departamento, con Swagger disponible.
- Todas las rutas públicas de la feature se diseñan bajo prefijo versionado `/api/v1/...`.
- Toda consulta de colección se diseña con 10 elementos por respuesta y orden determinista por `id` ascendente.
- La estrategia de pruebas cubre reglas de negocio críticas (unicidad, validación, borrado con relación, seguridad) en unidad e integración.
