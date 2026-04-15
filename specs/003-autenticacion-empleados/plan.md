# Implementation Plan: Autenticación de empleados

**Branch**: `003-autenticacion-empleados` | **Date**: 2026-03-11 | **Spec**: /specs/003-autenticacion-empleados/spec.md
**Input**: Feature specification from `/specs/003-autenticacion-empleados/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Implementar autenticación de empleados con correo y contraseña sobre HTTP Basic Auth,
manteniendo el acceso a endpoints versionados `/api/v1/empleados/**` para cualquier
empleado autenticado, sin endpoint dedicado de login. La solución incorporará política de
contraseña mínima (8 caracteres, al menos 1 letra y 1 número), bloqueo temporal de cuenta
por 15 minutos tras 5 intentos fallidos consecutivos, desbloqueo automático con reinicio
de contador, persistencia en PostgreSQL (entorno local con Docker Compose), actualización
de contrato OpenAPI/Swagger y cobertura de pruebas unitarias e integración para seguridad,
persistencia y reglas de bloqueo.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.4.x (Web, Security, Validation, Data JPA), Flyway, PostgreSQL driver, springdoc-openapi  
**Storage**: PostgreSQL (desarrollo/local con Docker Compose)  
**Testing**: JUnit 5, Spring Boot Test, Spring Security Test, Testcontainers PostgreSQL  
**Target Platform**: Linux server (API backend stateless)
**Project Type**: web-service backend REST  
**Performance Goals**: al menos 95% de autenticaciones válidas en <2s en entorno de validación  
**Constraints**: Basic Auth obligatorio, sin endpoint `/auth/login`, rutas públicas `/api/v1/...`, paginación fija de 10, correo único, contraseña mínima 8 con letra+número, bloqueo 15 min tras 5 fallos y desbloqueo automático con reinicio de contador  
**Scale/Scope**: módulo backend único para gestión interna de empleados autenticados con CRUD completo

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

[Gates determined based on constitution file]

- Estado pre-Phase 0: PASS

- Runtime y framework fijados: Java 17 + Spring Boot 3 (PASS).
- Autenticación definida: HTTP Basic Auth en `/api/v1/empleados/**`, sin excepción (PASS).
- Persistencia y entorno local: PostgreSQL + Docker Compose (PASS).
- Documentación de API: contrato OpenAPI y Swagger UI actualizados (PASS).
- Versionado en rutas públicas: prefijo `/api/v1` mantenido (PASS).
- Paginación de colecciones: tamaño fijo de 10 para listados (PASS).
- Estrategia de pruebas: unitarias + integración para seguridad y persistencia (PASS).

## Project Structure

### Documentation (this feature)

```text
specs/003-autenticacion-empleados/
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

**Structure Decision**: Se adopta estructura de backend Spring Boot única, con capas
de configuración, controlador, servicio, repositorio y dominio ya existentes. La
implementación de autenticación con correo/contraseña se integra sobre el módulo
actual de empleados y migraciones Flyway, manteniendo pruebas unitarias e integración
en la jerarquía de tests vigente.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
| --------- | ---------- | ------------------------------------ |
| Ninguna   | N/A        | N/A                                  |

## Post-Design Constitution Check

Estado post-Phase 1: PASS

- El diseño de modelo incorpora credenciales de empleado y eventos de autenticación sin romper Java 17 + Spring Boot 3.
- La estrategia de seguridad mantiene HTTP Basic Auth y define comportamiento de recursos protegidos sin endpoint de login dedicado.
- La persistencia se mantiene en PostgreSQL con provisión local por Docker Compose.
- El contrato OpenAPI documenta seguridad Basic Auth y cambios de payload/errores asociados a autenticación.
- Todas las rutas públicas continúan bajo `/api/v1/...`.
- Las consultas de colección preservan límite de 10 ítems por respuesta.
- El plan de pruebas contempla cobertura unitaria e integración para autenticación, bloqueo/desbloqueo y acceso a endpoints protegidos.
