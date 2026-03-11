# Implementation Plan: CRUD de empleados

**Branch**: `001-crud-empleados` | **Date**: 2026-02-26 | **Spec**: /specs/001-crud-empleados/spec.md
**Input**: Feature specification from `/specs/001-crud-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar un CRUD REST de empleados con operaciones de alta, consulta, actualización y
eliminación sobre la entidad `Empleado` con clave externa `EMP-<n>` autogenerada y PK
compuesta interna (`prefijo`, `numero_autonumerico`), manteniendo límites de 100 caracteres
para `nombre`, `direccion` y `telefono`, rutas públicas versionadas bajo `/api/v1` y
paginación fija de 10 resultados por consulta de colección. La implementación seguirá la
constitución del proyecto: Spring Boot 3 + Java 17, autenticación HTTP Basic Auth,
PostgreSQL en Docker para entorno local y documentación OpenAPI/Swagger actualizada.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3 (Web, Validation, Security, Data JPA), springdoc-openapi  
**Storage**: PostgreSQL (entorno local con Docker Compose)  
**Testing**: JUnit 5 + Spring Boot Test + MockMvc + Testcontainers (PostgreSQL)  
**Target Platform**: Linux server (API backend)
**Project Type**: web-service backend REST  
**Performance Goals**: p95 < 2s para consulta por clave en entorno de validación  
**Constraints**: Basic Auth obligatorio, longitud máxima 100 para campos textuales, clave autogenerada `EMP-<n>`, rutas con versión `/api/v1`, paginación de 10 por página en listados, sin cambio de `prefijo`/`numero_autonumerico` en update  
**Scale/Scope**: módulo único de empleados para uso administrativo interno

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

- Estado pre-Phase 0: PASS

- Runtime y framework fijados: Java 17 + Spring Boot 3 (PASS).
- Autenticación definida: HTTP Basic Auth en endpoints de negocio (PASS).
- Persistencia y entorno local: PostgreSQL + Docker Compose (PASS).
- Documentación de API: contrato OpenAPI + Swagger UI (PASS).
- Versionado de rutas: todos los endpoints públicos usan prefijo `/api/v1` (PASS).
- Paginación de colecciones: listado devuelve 10 resultados por consulta (PASS).
- Estrategia de pruebas: unitarias e integración para flujos funcionales y seguridad (PASS).

## Project Structure

### Documentation (this feature)

```text
specs/001-crud-empleados/
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

**Structure Decision**: Se selecciona estructura de servicio web backend única con
convención estándar de Spring Boot. Se mantiene separación explícita entre capa de
controlador, servicio, repositorio y dominio para facilitar pruebas y trazabilidad.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
| --------- | ---------- | ------------------------------------ |
| Ninguna   | N/A        | N/A                                  |

## Post-Design Constitution Check

Estado post-Phase 1: PASS

- Diseño de entidad y validaciones preserva restricciones de 100 caracteres, clave autogenerada `EMP-<n>` y PK compuesta estable.
- Contrato OpenAPI incluye endpoints CRUD y respuestas de error esperadas.
- Contrato OpenAPI define rutas versionadas `/api/v1` y paginación fija de 10 en listados.
- Quickstart define ejecución local con PostgreSQL en Docker.
- Plan de pruebas incluye cobertura de seguridad Basic Auth y persistencia.
