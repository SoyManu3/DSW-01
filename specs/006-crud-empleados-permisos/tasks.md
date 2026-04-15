# Tasks: CRUD visual de empleados con permisos por rol

**Input**: Design documents from `/specs/006-crud-empleados-permisos/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/empleados-ui-permissions-contract.md, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la especificación exige resultados medibles por rol y la constitución del proyecto exige cobertura automatizada proporcional.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura mínima del módulo visual de empleados en frontend.

- [x] T001 [US1] Crear estructura base de módulo empleados en `frontend/src/app/pages/empleados/`
- [x] T002 [P] [US1] Crear modelos del dominio UI de empleados en `frontend/src/app/services/empleados/empleados.models.ts`
- [x] T003 [P] [US1] Crear servicio HTTP base de empleados en `frontend/src/app/services/empleados/empleados.service.ts`
- [x] T004 [P] [US1] Registrar rutas base del módulo empleados en `frontend/src/app/app.routes.ts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura de autorización y contrato común que bloquea historias.

**⚠️ CRITICAL**: No iniciar historias hasta completar esta fase.

- [x] T005 [US1] Extender contrato de autenticación para garantizar `rol` en login en `src/main/java/com/example/empleados/controller/dto/LoginResponse.java`
- [x] T006 [US1] Alinear asignación de authorities por rol de empleado en `src/main/java/com/example/empleados/config/EmpleadoUserDetailsService.java`
- [x] T007 [P] [US1] Endurecer restricción de escritura solo admin en seguridad HTTP para `/api/v1/empleados/**` en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [x] T008 [P] [US2] Crear guard de rol admin para rutas de escritura en `frontend/src/app/guards/admin.guard.ts`
- [x] T009 [P] [US2] Añadir utilitario de permisos UI derivados de rol en `frontend/src/app/services/auth/role-permissions.ts`
- [x] T010 [US2] Integrar guard de autenticación + rol en rutas `/empleados`, `/empleados/:clave`, `/empleados/nuevo`, `/empleados/:clave/editar` en `frontend/src/app/app.routes.ts`
- [x] T011 [US1] Documentar contrato final de permisos UI por rol en `specs/006-crud-empleados-permisos/contracts/empleados-ui-permissions-contract.md`

**Checkpoint**: Fundaciones listas, se puede desarrollar historias en paralelo.

---

## Phase 3: User Story 1 - Administración completa por usuario admin (Priority: P1) 🎯 MVP

**Goal**: Permitir CRUD completo de empleados para sesión con rol `ADMIN`.

**Independent Test**: Iniciar sesión como admin, crear/editar/eliminar empleado desde UI y verificar refresco del listado.

### Tests for User Story 1

- [x] T012 [P] [US1] Crear pruebas unitarias del servicio de empleados (listar/crear/editar/eliminar) en `frontend/src/app/services/empleados/empleados.service.spec.ts`
- [x] T013 [P] [US1] Crear prueba de integración backend para autorización admin en CRUD de empleados en `src/test/java/com/example/empleados/integration/EmpleadoCrudAdminAuthorizationIntegrationTest.java`
- [x] T014 [P] [US1] Crear E2E de flujo CRUD admin en `frontend/cypress/e2e/empleados/empleados-admin-crud.cy.ts`

### Implementation for User Story 1

- [x] T015 [P] [US1] Implementar página de listado de empleados para admin en `frontend/src/app/pages/empleados/empleados-list.page.ts`
- [x] T016 [P] [US1] Implementar plantilla del listado con acciones CRUD admin en `frontend/src/app/pages/empleados/empleados-list.page.html`
- [x] T017 [P] [US1] Implementar estilos del listado de empleados en `frontend/src/app/pages/empleados/empleados-list.page.css`
- [x] T018 [P] [US1] Implementar página de creación de empleado en `frontend/src/app/pages/empleados/empleado-create.page.ts`
- [x] T019 [P] [US1] Implementar formulario y validaciones de creación en `frontend/src/app/pages/empleados/empleado-create.page.html`
- [x] T020 [P] [US1] Implementar página de edición de empleado en `frontend/src/app/pages/empleados/empleado-edit.page.ts`
- [x] T021 [P] [US1] Implementar formulario y validaciones de edición en `frontend/src/app/pages/empleados/empleado-edit.page.html`
- [x] T022 [US1] Conectar creación/edición/eliminación con refresco de listado en `frontend/src/app/pages/empleados/empleados-list.page.ts`
- [x] T023 [US1] Añadir interceptor de Basic Auth para endpoints de empleados en `frontend/src/app/interceptors/auth.interceptor.ts`

**Checkpoint**: US1 funcional y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Consulta segura por usuario empleado (Priority: P2)

**Goal**: Permitir solo lectura a usuarios `EMPLEADO` y bloquear modificaciones.

**Independent Test**: Iniciar sesión como empleado, verificar modo lectura y ausencia de operaciones de escritura efectivas.

### Tests for User Story 2

- [x] T024 [P] [US2] Crear prueba unitaria de `adminGuard` para redirección de empleado en `frontend/src/app/guards/admin.guard.spec.ts`
- [x] T025 [P] [US2] Crear prueba de integración backend que rechaza escritura de empleado en `src/test/java/com/example/empleados/integration/EmpleadoCrudEmpleadoAuthorizationIntegrationTest.java`
- [x] T026 [P] [US2] Crear E2E de empleado en solo lectura y bloqueo de acciones en `frontend/cypress/e2e/empleados/empleados-readonly.cy.ts`

### Implementation for User Story 2

- [x] T027 [US2] Aplicar matriz de permisos para ocultar/deshabilitar acciones de escritura en `frontend/src/app/pages/empleados/empleados-list.page.ts`
- [x] T028 [US2] Reflejar estado visual de solo lectura y mensaje contextual en `frontend/src/app/pages/empleados/empleados-list.page.html`
- [x] T029 [US2] Implementar redirección por acceso URL no autorizado a crear/editar en `frontend/src/app/guards/admin.guard.ts`
- [x] T030 [US2] Mapear respuestas 401/403 de escritura a mensaje de permisos insuficientes en `frontend/src/app/services/empleados/empleados.service.ts`

**Checkpoint**: US2 funcional e independiente sin depender de flujos de modificación.

---

## Phase 5: User Story 3 - Experiencia clara según rol al iniciar sesión (Priority: P3)

**Goal**: Comunicar claramente capacidades por rol y evitar confusión al cambiar sesión.

**Independent Test**: Alternar sesión admin/empleado y verificar actualización inmediata de permisos y mensajes.

### Tests for User Story 3

- [x] T031 [P] [US3] Crear prueba unitaria de estado de sesión y rol en `frontend/src/app/services/auth/auth.service.spec.ts`
- [x] T032 [P] [US3] Crear E2E de cambio de sesión entre roles en `frontend/cypress/e2e/empleados/empleados-role-switch.cy.ts`

### Implementation for User Story 3

- [x] T033 [US3] Mostrar indicador de rol activo y capacidades en pantalla de empleados en `frontend/src/app/pages/empleados/empleados-list.page.html`
- [x] T034 [US3] Añadir lógica de limpieza y rehidratación consistente de rol al login/logout en `frontend/src/app/services/auth/auth.service.ts`
- [x] T035 [US3] Actualizar página de inicio para navegación al módulo empleados con contexto de rol en `frontend/src/app/pages/inicio/inicio.page.ts`

**Checkpoint**: US3 funcional e independiente con UX consistente por rol.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidar calidad transversal, documentación y validación final.

- [x] T036 [P] [US3] Actualizar documentación de uso por rol en `frontend/README.md`
- [x] T037 [P] [US3] Actualizar guía de ejecución y evidencias en `specs/006-crud-empleados-permisos/quickstart.md`
- [x] T038 [US1] Verificar y ajustar contrato OpenAPI de empleados para seguridad por rol en `specs/006-crud-empleados-permisos/contracts/empleados-permisos.openapi.yaml`
- [x] T039 [US2] Ejecutar suite de pruebas frontend para permisos por rol desde `frontend/cypress/e2e/empleados/`
- [x] T040 [US1] Ejecutar pruebas de integración backend de autorización en `src/test/java/com/example/empleados/integration/`
- [x] T041 [P] [US2] Implementar página de detalle de empleado en modo lectura en `frontend/src/app/pages/empleados/empleado-detail.page.ts`
- [x] T042 [P] [US2] Implementar plantilla de detalle de empleado en modo lectura en `frontend/src/app/pages/empleados/empleado-detail.page.html`
- [x] T043 [US2] Integrar consulta por clave para detalle de empleado en `frontend/src/app/services/empleados/empleados.service.ts`
- [x] T044 [P] [US1] Crear prueba de integración de paginación fija (size=10) en listado de empleados en `src/test/java/com/example/empleados/integration/EmpleadoPaginationSizeIntegrationTest.java`
- [x] T045 [US3] Definir y registrar protocolo de medición de SC-004 (20 ejecuciones) en `specs/006-crud-empleados-permisos/checklists/usabilidad-roles.md`
- [x] T046 [US1] Verificar evidencia de stack obligatorio (Spring Boot 3 + Java 17 + Angular 21) en `pom.xml`, `frontend/package.json` y registrar resultado en `specs/006-crud-empleados-permisos/checklists/requirements.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Phase 2.
- **Phase 4 (US2)**: depende de Phase 2; puede avanzar en paralelo con US1 salvo tareas sobre mismos archivos.
- **Phase 5 (US3)**: depende de Phase 2; puede avanzar después de contar con UI base de empleados.
- **Phase 6 (Polish)**: depende de completar historias priorizadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras fundaciones; entrega MVP.
- **US2 (P2)**: inicia tras fundaciones; reutiliza componentes de listado pero debe probarse en forma independiente.
- **US3 (P3)**: inicia tras fundaciones y sobre experiencia final de sesión.

### Within Each User Story

- Pruebas primero (deben fallar antes de implementar).
- Servicio/modelos antes de páginas.
- Guard/interceptor antes de validación E2E final.
- Cerrar cada historia con checkpoint funcional independiente.

### Parallel Opportunities

- Setup: T002, T003 y T004 en paralelo.
- Foundational: T007, T008 y T009 en paralelo.
- US1: T012, T013 y T014 en paralelo; T015-T021 pueden repartirse por archivos distintos.
- US2: T024, T025 y T026 en paralelo.
- US3: T031 y T032 en paralelo.

---

## Parallel Example: User Story 1

```bash
# Ejecutar pruebas base de US1 en paralelo
T012 frontend/src/app/services/empleados/empleados.service.spec.ts
T013 src/test/java/com/example/empleados/integration/EmpleadoCrudAdminAuthorizationIntegrationTest.java
T014 frontend/cypress/e2e/empleados/empleados-admin-crud.cy.ts

# Implementar pantallas US1 en paralelo
T018 frontend/src/app/pages/empleados/empleado-create.page.ts
T020 frontend/src/app/pages/empleados/empleado-edit.page.ts
T016 frontend/src/app/pages/empleados/empleados-list.page.html
```

## Parallel Example: User Story 2

```bash
# Pruebas de restricciones por rol en paralelo
T024 frontend/src/app/guards/admin.guard.spec.ts
T025 src/test/java/com/example/empleados/integration/EmpleadoCrudEmpleadoAuthorizationIntegrationTest.java
T026 frontend/cypress/e2e/empleados/empleados-readonly.cy.ts
```

## Parallel Example: User Story 3

```bash
# Validación de consistencia de sesión por rol
T031 frontend/src/app/services/auth/auth.service.spec.ts
T032 frontend/cypress/e2e/empleados/empleados-role-switch.cy.ts
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup y Foundational.
2. Implementar US1 completa.
3. Validar pruebas y demo de CRUD admin.

### Incremental Delivery

1. Entregar MVP con US1.
2. Agregar US2 para seguridad de lectura en empleado.
3. Agregar US3 para claridad de UX y consistencia al cambiar sesión.
4. Cerrar con Polish y validación quickstart.

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego:
   - Dev A: US1 (CRUD admin)
   - Dev B: US2 (solo lectura empleado)
   - Dev C: US3 (mensajería y consistencia por rol)
3. Integración y validación cruzada en Phase 6.
