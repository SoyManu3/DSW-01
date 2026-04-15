# Implementation Plan: CRUD visual de departamentos con permisos por rol

**Branch**: `007-crud-departamentos-roles` | **Date**: 2026-03-26 | **Spec**: `/specs/007-crud-departamentos-roles/spec.md`
**Input**: Feature specification from `/specs/007-crud-departamentos-roles/spec.md`

## Summary

Implementar la capa visual CRUD de departamentos en Angular 21 integrada al login existente con control por rol entregado por backend: `ADMIN` con operaciones completas de crear/editar/eliminar y `EMPLEADO` en modo lectura con acciones visibles deshabilitadas. El diseño incluye guardas de rutas de escritura, redirección por acceso no autorizado a listado, mensajes estandarizados de permisos/sesión y uso de endpoints existentes bajo `/api/v1/departamentos` con paginación de 10 elementos.

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.2 (backend existente)  
**Primary Dependencies**: Angular Router, Angular Forms, HttpClient, RxJS, Vitest/Karma, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi  
**Storage**: PostgreSQL para datos de negocio y `localStorage` para estado de sesión UI (`Authorization` + rol)  
**Testing**: Unit tests frontend (servicios/guards/componentes), Cypress E2E para permisos por rol y regresión de rutas, JUnit/Spring Security Test para integraciones backend relacionadas  
**Target Platform**: Aplicación web en navegador moderno + backend Linux local/dev con Docker Compose
**Project Type**: Aplicación web full-stack (frontend Angular + backend Spring Boot)  
**Performance Goals**: Render inicial de listado y transición de navegación de rutas protegidas en tiempo percibido fluido (<2s local) sin degradar reglas de autorización  
**Constraints**: HTTP Basic Auth obligatorio, rutas versionadas `/api/v1/...`, consultas de colección paginadas en tamaño fijo 10, mensajes de permisos/sesión definidos en spec, sin hardcode de credenciales  
**Scale/Scope**: 1 nuevo módulo visual de departamentos con 2 perfiles (`ADMIN`, `EMPLEADO`), rutas protegidas para escritura y validación E2E de matriz de permisos

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

### Pre-Phase 0 Gate

- ✅ Java runtime fijado en 17 y backend sobre Spring Boot 3.4.x.
- ✅ Autenticación definida como HTTP Basic Auth para recursos protegidos.
- ✅ Persistencia en PostgreSQL y provisión local/dev con Docker Compose.
- ✅ Contratos API y documentación OpenAPI/Swagger contemplados en artefactos de feature.
- ✅ Rutas públicas con versionado explícito `/api/v1/...`.
- ✅ Colecciones con paginación determinista de 10 instancias por consulta.
- ✅ Estrategia de pruebas cubre flujos críticos de autorización (unit + integración/E2E).

## Project Structure

### Documentation (this feature)

```text
specs/007-crud-departamentos-roles/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── departamentos-ui-permissions-contract.md
│   └── departamentos-permisos.openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/java/com/example/empleados/
│   ├── config/
│   ├── controller/
│   ├── controller/dto/
│   ├── domain/
│   ├── repository/
│   └── service/
└── test/java/com/example/empleados/

frontend/
├── src/app/
│   ├── guards/
│   ├── pages/
│   │   ├── login/
│   │   ├── inicio/
│   │   ├── empleados/
│   │   └── departamentos/
│   ├── services/
│   │   ├── auth/
│   │   └── departamentos/
│   └── app.routes.ts
└── cypress/e2e/
```

**Structure Decision**: Se mantiene la estructura de aplicación web existente y se extiende el frontend Angular con nuevas páginas/servicios de departamentos reutilizando el backend ya disponible para CRUD y seguridad, permitiendo únicamente ajustes mínimos de autorización en backend (sin crear nuevos módulos).

## Post-Design Constitution Check

- ✅ Diseño mantiene Angular 21 LTS como capa visual principal.
- ✅ Control de acceso usa HTTP Basic Auth y rol de sesión (`ADMIN`/`EMPLEADO`) sin excepciones.
- ✅ Persistencia sigue en PostgreSQL + Docker Compose; no se introduce almacenamiento alternativo de negocio.
- ✅ Interfaz frontend consume endpoints documentados y contratos OpenAPI de departamentos.
- ✅ Todas las rutas consumidas conservan prefijo `/api/v1/...`.
- ✅ Listados de departamentos y empleados por departamento mantienen tamaño de página fijo 10.
- ✅ Plan de pruebas incluye cobertura de seguridad/autorización por rutas y acciones UI.

## Complexity Tracking

Sin violaciones constitucionales; no requiere excepciones.
