# Implementation Plan: Pantallas de Login de Empleados

**Branch**: `005-pantallas-login-empleados` | **Date**: 2026-03-25 | **Spec**: `/specs/005-pantallas-login-empleados/spec.md`
**Input**: Feature specification from `/specs/005-pantallas-login-empleados/spec.md`

## Summary

Implementar el flujo visual de login de empleados en Angular 21 LTS con pantalla de acceso por email corporativo + contraseña, manejo de estados (inicial, validación, cargando, error, éxito), redirección única a `/inicio`, y pruebas E2E con Cypress contra backend local real usando autenticación HTTP Basic existente.

## Technical Context

**Language/Version**: TypeScript (Angular 21 LTS), Java 17 (backend existente)  
**Primary Dependencies**: Angular 21 LTS, Angular Router, Angular Forms, Cypress; Spring Boot 3.4.x, Spring Security (backend)  
**Storage**: PostgreSQL (backend existente), sin persistencia adicional en frontend  
**Testing**: Cypress E2E (frontend), pruebas backend existentes con JUnit + Spring Security Test + Testcontainers  
**Target Platform**: Navegador web moderno en entorno Linux local para desarrollo/pruebas  
**Project Type**: Aplicación web (frontend Angular + backend Spring Boot)  
**Performance Goals**: Cumplir SC-001 (p95 <= 60s en 30 corridas E2E consecutivas de login exitoso) y SC-003 (>=95% éxito E2E repetible)  
**Constraints**: Angular 21 LTS obligatorio, HTTP Basic Auth sin endpoint de login dedicado, mensaje genérico `Credenciales inválidas`, accesibilidad base WCAG 2.1 AA, redirección post-login a `/inicio`  
**Scale/Scope**: 1 flujo de login con estados visuales clave, 1 ruta protegida de aterrizaje, suite E2E para éxito/fallo/validación

## Constitution Check

_GATE: Must pass before Phase 0 research. Re-check after Phase 1 design._

### Pre-Phase 0 Gate

- ✅ Java runtime y framework backend compatibles con constitución (`Java 17` + `Spring Boot 3`).
- ✅ Autenticación explícita con `HTTP Basic Auth` y sin excepción no justificada.
- ✅ Persistencia en `PostgreSQL` con aprovisionamiento local mediante `Docker`/Compose ya definido.
- ✅ Contrato API con `OpenAPI/Swagger` existente y reutilizado por el frontend.
- ✅ Versionado explícito de rutas públicas (`/api/v1/...`) mantenido.
- ✅ Paginación de colecciones (10 ítems) no se altera en esta feature; se respeta restricción constitucional.
- ✅ Estrategia de pruebas incluye automatización E2E de flujo crítico y validación de integración UI+API.

## Project Structure

### Documentation (this feature)

```text
specs/005-pantallas-login-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
└── tasks.md
```

### Source Code (repository root)

```text
src/
├── main/java/com/example/empleados/
│   ├── config/
│   ├── controller/
│   ├── domain/
│   ├── repository/
│   └── service/
└── test/java/com/example/empleados/

frontend/                       # nuevo proyecto Angular 21 LTS en esta feature
├── src/
│   ├── app/
│   │   ├── pages/login/
│   │   ├── pages/inicio/
│   │   ├── services/auth/
│   │   ├── guards/
│   │   └── interceptors/
│   └── assets/
└── cypress/
    └── e2e/login/
```

**Structure Decision**: Se adopta estructura web application con backend Spring Boot existente y nuevo `frontend/` Angular 21 para aislar capa visual y habilitar Cypress E2E contra API real local.

## Post-Design Constitution Check

- ✅ Diseño mantiene Angular 21 LTS como única tecnología visual del sistema.
- ✅ Integración de login alineada con HTTP Basic Auth y sin introducir endpoint de login no contemplado.
- ✅ Diseño de pruebas E2E contra backend real local preserva validación de seguridad e integración.
- ✅ No se introducen cambios que violen versionado de API ni contrato OpenAPI existente.
- ✅ No hay desviaciones constitucionales que requieran justificación en Complexity Tracking.

## Complexity Tracking

Sin violaciones constitucionales; sección no aplica para esta feature.
