# Implementation Plan: CRUD visual de empleados con permisos por rol

**Branch**: `006-crud-empleados-permisos` | **Date**: 2026-03-26 | **Spec**: `/specs/006-crud-empleados-permisos/spec.md`
**Input**: Feature specification from `/specs/006-crud-empleados-permisos/spec.md`

## Summary

Implementar la interfaz CRUD de empleados en Angular 21 con control de permisos por rol proveniente del backend: `ADMIN` con operaciones completas (crear/editar/eliminar/listar) y `EMPLEADO` en modo solo lectura. La solución incluye protección de rutas de edición/creación, degradación segura de acciones no autorizadas (redirección a listado con mensaje), integración con endpoints existentes `/api/v1/empleados` y cobertura de pruebas de permisos por rol.

## Technical Context

**Language/Version**: TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente)  
**Primary Dependencies**: Angular Router, Angular Forms, HttpClient, RxJS, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi  
**Storage**: PostgreSQL (tabla `empleados` con campo `rol`), `localStorage` para estado de sesión UI (header Basic + rol)  
**Testing**: Unit tests Angular/Vitest o spec de componentes, Cypress E2E para permisos por rol, JUnit/Spring Security Test para backend relacionado  
**Target Platform**: Web app en navegador moderno + backend Linux local con Docker Compose
**Project Type**: Aplicación web full-stack (frontend Angular + backend Spring Boot)  
**Performance Goals**: Transición de login a pantalla de inicio y render de lista de empleados en tiempo percibido fluido (<2s local) manteniendo restricciones funcionales de SC-001..SC-004  
**Constraints**: HTTP Basic Auth obligatorio, rutas versionadas `/api/v1/...`, paginación de 10 ítems por consulta, sin hardcode de credenciales/roles fuera de configuración o respuesta backend  
**Scale/Scope**: 1 módulo visual de empleados con 2 perfiles (`ADMIN`, `EMPLEADO`), 1 conjunto de rutas protegidas, 1 contrato UI-permisos, pruebas por matriz de permisos

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

### Pre-Phase 0 Gate

- ✅ Runtime backend y framework alineados: Java 17 + Spring Boot 3.
- ✅ Mecanismo de autenticación explícito: HTTP Basic Auth mantenido para recursos protegidos.
- ✅ Persistencia en PostgreSQL con aprovisionamiento local vía Docker Compose ya disponible.
- ✅ Contratos API documentados en OpenAPI/Swagger y reutilizados por el frontend.
- ✅ Rutas públicas con versionado explícito `/api/v1/...`.
- ✅ Paginación de colecciones en tamaño fijo de 10 mantenida en listado de empleados.
- ✅ Estrategia de pruebas incluye validación de seguridad/autorización por rol en UI y API.

## Project Structure

### Documentation (this feature)

```text
specs/006-crud-empleados-permisos/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── empleados-ui-permissions-contract.md
│   └── empleados-permisos.openapi.yaml
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
├── main/resources/
│   ├── application.yml
│   └── db/migration/
└── test/java/com/example/empleados/

frontend/
├── src/
│   ├── app/
│   │   ├── guards/
│   │   ├── pages/
│   │   │   ├── login/
│   │   │   ├── inicio/
│   │   │   └── empleados/
│   │   ├── services/
│   │   │   ├── auth/
│   │   │   └── empleados/
│   │   └── app.routes.ts
│   └── environments/
└── cypress/e2e/
```

**Structure Decision**: Se mantiene estructura web application existente y se extiende el frontend Angular con nuevas páginas/guards/servicios de empleados; backend se reutiliza con ajustes mínimos de autorización y contrato si aplica.

## Post-Design Constitution Check

- ✅ Diseño mantiene Angular 21 como capa visual principal, sin frameworks alternativos.
- ✅ Control de acceso por rol se apoya en autenticación HTTP Basic existente y rol entregado por backend.
- ✅ Persistencia sigue en PostgreSQL/Flyway; no se introduce almacenamiento alternativo de negocio.
- ✅ Integración UI usa endpoints documentados con OpenAPI/Swagger.
- ✅ Todas las rutas consumidas permanecen en `/api/v1/...`.
- ✅ Listados siguen paginación determinista de 10 ítems.
- ✅ Plan de pruebas incluye escenarios de seguridad para evitar escalamiento de privilegios en UI.

## Complexity Tracking

Sin violaciones constitucionales; no requiere excepciones.
