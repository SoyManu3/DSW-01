# dsw2-practica01 Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-02-26

## Active Technologies
- PostgreSQL (001-pagination-10-items)
- Java 17 + Spring Boot 3.4.x (Web, Security, Validation, Data JPA), Flyway, PostgreSQL driver, springdoc-openapi (003-autenticacion-empleados)
- PostgreSQL (desarrollo/local con Docker Compose) (003-autenticacion-empleados)
- PostgreSQL (con Docker Compose en local/dev) (004-crud-departamentos)
- TypeScript 5.x, Angular 21 LTS + Angular 21 LTS, RxJS, Angular Material (opcional para UI), Node.js >=18.x para build/dev (005-login-empleados-visual)
- N/A (frontend visual, sin persistencia local) (005-login-empleados-visual)
- TypeScript 5.x, Angular 21 LTS + Angular 21 LTS, RxJS, Angular Router, Angular Forms, Angular Material (opcional para UI) (005-login-empleados-visual)
- N/A (frontend visual sin persistencia local obligatoria) (005-login-empleados-visual)
- TypeScript (Angular 21 LTS), Java 17 (backend existente) + Angular 21 LTS, Angular Router, Angular Forms, Cypress; Spring Boot 3.4.x, Spring Security (backend) (005-pantallas-login-empleados)
- PostgreSQL (backend existente), sin persistencia adicional en frontend (005-pantallas-login-empleados)
- TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente) + Angular Router, Angular Forms, HttpClient, RxJS, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi (006-crud-empleados-permisos)
- PostgreSQL (tabla `empleados` con campo `rol`), `localStorage` para estado de sesión UI (header Basic + rol) (006-crud-empleados-permisos)
- TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.2 (backend existente) + Angular Router, Angular Forms, HttpClient, RxJS, Vitest/Karma, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi (007-crud-departamentos-roles)
- PostgreSQL para datos de negocio y `localStorage` para estado de sesión UI (`Authorization` + rol) (007-crud-departamentos-roles)
- TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente), Docker Engine 26+ / Compose v2 + Angular CLI/build, Nginx (runtime estático), Docker Compose, Spring Security HTTP Basic, PostgreSQL 16 (008-dockerize-frontend-nginx)
- N/A para frontend estático; backend continúa con PostgreSQL (sin cambios de modelo) (008-dockerize-frontend-nginx)

- Java 17 + Spring Boot 3 (Web, Validation, Security, Data JPA), springdoc-openapi (001-crud-empleados)

## Project Structure

```text
backend/
frontend/
tests/
```

## Commands

# Add commands for Java 17

## Code Style

Java 17: Follow standard conventions

## Recent Changes
- 008-dockerize-frontend-nginx: Added TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente), Docker Engine 26+ / Compose v2 + Angular CLI/build, Nginx (runtime estático), Docker Compose, Spring Security HTTP Basic, PostgreSQL 16
- 007-crud-departamentos-roles: Added TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.2 (backend existente) + Angular Router, Angular Forms, HttpClient, RxJS, Vitest/Karma, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi
- 006-crud-empleados-permisos: Added TypeScript 5.9 + Angular 21.2.x (frontend), Java 17 + Spring Boot 3.4.x (backend existente) + Angular Router, Angular Forms, HttpClient, RxJS, Cypress; Spring Security HTTP Basic, Spring Data JPA, Flyway, springdoc-openapi


<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
