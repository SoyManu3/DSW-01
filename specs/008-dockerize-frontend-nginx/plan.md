# Implementation Plan: Contenedor Docker Frontend con Nginx

**Branch**: `008-dockerize-frontend-nginx` | **Date**: 2026-03-26 | **Spec**: `/specs/008-dockerize-frontend-nginx/spec.md`
**Input**: Feature specification from `/specs/008-dockerize-frontend-nginx/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

Containerizar el frontend Angular 21 en una imagen Docker multi-stage servida por Nginx e integrarlo al `docker/docker-compose.yml` existente como servicio `frontend`, con mapeo `4200:80`, variable de entorno `API_BASE_URL` (default `http://localhost:8080/api/v1`) y `healthcheck` HTTP obligatorio. La solución mantiene compatibilidad con backend Spring Boot + PostgreSQL, sin modificar contrato API público.

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the technical details
  for the project. The structure here is presented in advisory capacity to guide
  the iteration process.
-->

**Language/Version**: TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente), Docker Engine 26+ / Compose v2  
**Primary Dependencies**: Angular CLI/build, Nginx (runtime estático), Docker Compose, Spring Security HTTP Basic, PostgreSQL 16  
**Storage**: N/A para frontend estático; backend continúa con PostgreSQL (sin cambios de modelo)  
**Testing**: Prueba de build Docker frontend, smoke test HTTP (`/`), healthcheck Compose, regresión de arranque de stack (`postgres` + `api` + `frontend`)  
**Target Platform**: Desarrollo local Linux/macOS/Windows con Docker + navegador moderno
**Project Type**: Aplicación web full-stack (frontend Angular + backend Spring Boot)  
**Performance Goals**: Primer render del frontend servido por Nginx en entorno local < 2s tras disponibilidad del contenedor  
**Constraints**: Mantener puerto host `4200`, conservar rutas API `/api/v1/...`, no romper Basic Auth ni paginación de 10 en backend, sin hardcode de secretos  
**Scale/Scope**: 1 imagen frontend + 1 servicio nuevo en Compose + ajustes mínimos de documentación operativa

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

### Pre-Phase 0 Gate

- ✅ Backend permanece en Java 17 + Spring Boot 3.
- ✅ Se mantiene HTTP Basic Auth para recursos protegidos de API; el frontend solo consume endpoints autenticados existentes.
- ✅ Persistencia sigue en PostgreSQL y aprovisionamiento local continúa en Docker Compose.
- ✅ OpenAPI/Swagger existente se mantiene accesible; esta feature no introduce endpoints nuevos.
- ✅ Rutas públicas API consumidas por frontend conservan versionado explícito `/api/v1/...`.
- ✅ Paginación de colecciones de 10 elementos en backend no se altera por esta feature.
- ✅ Estrategia de pruebas incluye smoke de contenedor, healthcheck y regresión de arranque del stack completo.

## Project Structure

### Documentation (this feature)

```text
specs/008-dockerize-frontend-nginx/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   ├── frontend-compose-service-contract.md
│   └── frontend-runtime-config-contract.md
└── tasks.md
```

### Source Code (repository root)

<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->

```text
docker/
└── docker-compose.yml

frontend/
├── Dockerfile
├── nginx/
│   ├── default.conf
│   └── env.template.js
├── src/
│   └── environments/
└── package.json

src/main/java/com/example/empleados/
└── ... (sin cambios funcionales previstos)
```

**Structure Decision**: Se mantiene la arquitectura web existente y se extiende con artefactos de despliegue frontend dentro de `frontend/` más integración en `docker/docker-compose.yml`, evitando crear nuevos módulos backend o estructura paralela.

## Phase 0: Research

1. Definir patrón de build/runtime para Angular estático en Nginx (multi-stage).
2. Definir estrategia de inyección de `API_BASE_URL` en runtime para contenedor Nginx.
3. Definir healthcheck HTTP de Compose y comportamiento ante fallas de build/puerto.

## Phase 1: Design & Contracts

1. Modelar entidades operativas de despliegue (`FrontendImageArtifact`, `FrontendComposeService`, `FrontendRuntimeConfig`).
2. Definir contratos operativos en `contracts/` para servicio Compose y configuración runtime.
3. Documentar quickstart reproducible para build/arranque y validaciones mínimas.

## Post-Design Constitution Check

- ✅ Frontend continúa en Angular 21 LTS; Nginx se usa solo como servidor estático de artefactos compilados.
- ✅ No se modifican reglas de autenticación backend (HTTP Basic Auth) ni se exponen bypasses.
- ✅ Estrategia Docker Compose local se fortalece incorporando frontend al stack existente.
- ✅ OpenAPI/Swagger y contrato API backend permanecen vigentes sin breaking changes.
- ✅ Prefijo de versión `/api/v1/...` se mantiene mediante `API_BASE_URL` por defecto.
- ✅ Paginación de 10 ítems en colecciones backend no se altera.
- ✅ Validación incluye pruebas de ejecución de contenedor y disponibilidad de servicio frontend.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

Sin violaciones constitucionales; no se requieren excepciones.
