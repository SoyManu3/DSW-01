# Tasks: CRUD visual de departamentos con permisos por rol

**Input**: Design documents from `/specs/007-crud-departamentos-roles/`  
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base del módulo visual de departamentos y pruebas asociadas.

- [X] T001 Crear estructura del módulo en `frontend/src/app/pages/departamentos/` y `frontend/src/app/services/departamentos/`
- [X] T002 Crear estructura E2E del módulo en `frontend/cypress/e2e/departamentos/`
- [X] T003 [P] Preparar archivo de pruebas unitarias de servicio en `frontend/src/app/services/departamentos/departamentos.service.spec.ts`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura bloqueante de seguridad, rutas y contratos de comportamiento transversal.

**⚠️ CRITICAL**: Ninguna historia de usuario puede comenzar hasta completar esta fase.

- [X] T004 Ajustar autorización por método HTTP para departamentos en `src/main/java/com/example/empleados/config/SecurityConfig.java`
- [X] T005 [P] Agregar pruebas de integración de autorización para departamentos en `src/test/java/com/example/empleados/integration/DepartamentoSecurityIntegrationTest.java`
- [X] T006 Crear modelos de dominio UI de departamentos en `frontend/src/app/services/departamentos/departamentos.models.ts`
- [X] T007 Crear servicio HTTP base de departamentos en `frontend/src/app/services/departamentos/departamentos.service.ts`
- [X] T008 [P] Crear guard de admin para rutas de escritura de departamentos en `frontend/src/app/guards/departamentos-admin.guard.ts`
- [X] T009 [P] Agregar pruebas del guard de admin de departamentos en `frontend/src/app/guards/departamentos-admin.guard.spec.ts`
- [X] T010 Actualizar `authGuard` para redirección con señal de sesión inválida en `frontend/src/app/guards/auth.guard.ts`
- [X] T011 Configurar lectura y visualización de mensaje de sesión inválida en `frontend/src/app/pages/login/login.page.ts` y `frontend/src/app/pages/login/login.page.html`
- [X] T012 Registrar rutas protegidas de departamentos en `frontend/src/app/app.routes.ts`
- [X] T013 Enlazar navegación al módulo de departamentos desde inicio en `frontend/src/app/pages/inicio/inicio.page.html`

**Checkpoint**: Fundación lista; las historias de usuario pueden implementarse en paralelo.

---

## Phase 3: User Story 1 - Gestión completa para admin (Priority: P1) 🎯 MVP

**Goal**: Permitir a `ADMIN` ejecutar CRUD completo de departamentos desde la interfaz.

**Independent Test**: Iniciar sesión como admin, abrir `/departamentos` y completar alta, edición y eliminación verificando refresco del listado.

### Tests for User Story 1

- [X] T014 [P] [US1] Implementar pruebas unitarias de CRUD en `frontend/src/app/services/departamentos/departamentos.service.spec.ts`
- [X] T015 [P] [US1] Implementar escenario E2E admin CRUD en `frontend/cypress/e2e/departamentos/departamentos-admin-crud.cy.ts`
- [X] T016 [P] [US1] Añadir aserciones explícitas de paginación `size=10` y navegación en `frontend/src/app/services/departamentos/departamentos.service.spec.ts` y `frontend/cypress/e2e/departamentos/departamentos-admin-crud.cy.ts`

### Implementation for User Story 1

- [X] T017 [P] [US1] Crear vista de listado de departamentos en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`
- [X] T018 [P] [US1] Crear plantilla de listado de departamentos en `frontend/src/app/pages/departamentos/departamentos-list.page.html`
- [X] T019 [P] [US1] Crear estilos de listado de departamentos en `frontend/src/app/pages/departamentos/departamentos-list.page.css`
- [X] T020 [P] [US1] Crear estilos compartidos de formulario en `frontend/src/app/pages/departamentos/departamento-form.page.css`
- [X] T021 [US1] Implementar página de creación en `frontend/src/app/pages/departamentos/departamento-create.page.ts`
- [X] T022 [US1] Implementar plantilla de creación en `frontend/src/app/pages/departamentos/departamento-create.page.html`
- [X] T023 [US1] Implementar página de edición en `frontend/src/app/pages/departamentos/departamento-edit.page.ts`
- [X] T024 [US1] Implementar plantilla de edición en `frontend/src/app/pages/departamentos/departamento-edit.page.html`
- [X] T025 [US1] Implementar página de detalle en `frontend/src/app/pages/departamentos/departamento-detail.page.ts`
- [X] T026 [US1] Implementar plantilla de detalle en `frontend/src/app/pages/departamentos/departamento-detail.page.html`
- [X] T027 [US1] Integrar acciones admin (crear/editar/eliminar) y refresco de lista en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`

**Checkpoint**: US1 funcional e independiente (MVP).

---

## Phase 4: User Story 2 - Consulta en modo lectura para empleado (Priority: P2)

**Goal**: Permitir a `EMPLEADO` visualizar departamentos con acciones CRUD visibles pero deshabilitadas y bloqueadas.

**Independent Test**: Iniciar sesión como empleado y verificar listado/consulta sin operaciones de escritura exitosas.

### Tests for User Story 2

- [X] T028 [P] [US2] Agregar pruebas de permisos visuales en `frontend/src/app/pages/departamentos/departamentos-list.page.spec.ts`
- [X] T029 [P] [US2] Implementar escenario E2E de solo lectura en `frontend/cypress/e2e/departamentos/departamentos-readonly.cy.ts`
- [X] T030 [P] [US2] Verificar acceso de `EMPLEADO` a detalle en modo lectura en `frontend/src/app/pages/departamentos/departamento-detail.page.spec.ts` y `frontend/cypress/e2e/departamentos/departamentos-readonly.cy.ts`

### Implementation for User Story 2

- [X] T031 [US2] Renderizar controles CRUD deshabilitados para empleado en `frontend/src/app/pages/departamentos/departamentos-list.page.html`
- [X] T032 [US2] Bloquear handlers de crear/editar/eliminar para empleado en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`
- [X] T033 [US2] Redirigir empleado en rutas de escritura con `denied=1` en `frontend/src/app/guards/departamentos-admin.guard.ts`
- [X] T034 [US2] Mostrar mensaje global de permisos denegados en listado en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`

**Checkpoint**: US2 funcional e independiente.

---

## Phase 5: User Story 3 - Consistencia de permisos entre sesiones (Priority: P3)

**Goal**: Garantizar que la UI actualice permisos correctamente al cambiar sesión y ante sesión expirada.

**Independent Test**: Alternar sesión admin↔empleado y verificar actualización de capacidades; simular sesión inválida y validar redirección a login.

### Tests for User Story 3

- [X] T035 [P] [US3] Implementar escenario E2E de cambio de rol en `frontend/cypress/e2e/departamentos/departamentos-role-switch.cy.ts`
- [X] T036 [P] [US3] Ampliar pruebas de guard para sesión inválida en `frontend/src/app/guards/auth.guard.spec.ts`
- [X] T037 [P] [US3] Verificar consistencia de permisos tras recarga en `frontend/src/app/pages/departamentos/departamentos-list.page.spec.ts`
- [X] T038 [P] [US3] Implementar escenario E2E de recarga manteniendo permisos por rol en `frontend/cypress/e2e/departamentos/departamentos-role-switch.cy.ts`

### Implementation for User Story 3

- [X] T039 [US3] Sincronizar estado de rol/sesión entre login y logout en `frontend/src/app/services/auth/auth.service.ts`
- [X] T040 [US3] Validar flujo completo de redirección por sesión expirada y mensaje en login sobre `frontend/src/app/guards/auth.guard.ts` y `frontend/src/app/pages/login/login.page.ts`
- [X] T041 [US3] Asegurar persistencia de estado de permisos tras recarga en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`
- [X] T042 [US3] Alinear cierre de sesión y navegación en módulo departamentos en `frontend/src/app/pages/departamentos/departamentos-list.page.ts`

**Checkpoint**: US3 funcional e independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de calidad, documentación y validaciones cruzadas.

- [X] T043 [P] Verificar consistencia entre contrato UI y comportamiento implementado en `specs/007-crud-departamentos-roles/contracts/departamentos-ui-permissions-contract.md`
- [X] T044 [P] Verificar consistencia entre endpoints usados y OpenAPI en `specs/007-crud-departamentos-roles/contracts/departamentos-permisos.openapi.yaml`
- [X] T045 [P] Verificar cumplimiento CA-001 (stack Java 17/Spring Boot 3/Angular 21) en `pom.xml`, `frontend/package.json` y `specs/007-crud-departamentos-roles/quickstart.md`
- [X] T046 [P] Verificar cumplimiento CA-003 (PostgreSQL + Docker Compose) en `docker/docker-compose.yml` y `specs/007-crud-departamentos-roles/quickstart.md`
- [X] T047 [US3] Definir protocolo y plantilla de registro para SC-003 en `specs/007-crud-departamentos-roles/quickstart.md` y `specs/007-crud-departamentos-roles/checklists/usabilidad-sc003.md`
- [ ] T048 [US3] Ejecutar medición SC-003 y registrar evidencia en `specs/007-crud-departamentos-roles/checklists/usabilidad-sc003.md`
- [X] T049 Actualizar pasos finales/evidencias en `specs/007-crud-departamentos-roles/quickstart.md`
- [X] T050 Documentar decisiones finales de implementación en `specs/007-crud-departamentos-roles/research.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: puede iniciar inmediatamente.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3-5 (User Stories)**: dependen de completar Phase 2.
- **Phase 6 (Polish)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; define MVP.
- **US2 (P2)**: inicia tras Foundational; reutiliza componentes de listado/rutas, pero su validación es independiente.
- **US3 (P3)**: inicia tras Foundational; depende lógicamente de autenticación/guards, validación independiente.

### Suggested Delivery Order

1. Setup + Foundational
2. US1 (MVP)
3. US2
4. US3
5. Polish

---

## Parallel Execution Examples

### User Story 1

- Ejecutar en paralelo T014, T015 y T016 (pruebas unitarias y E2E de admin).
- Ejecutar en paralelo T017, T018, T019 y T020 (archivos separados de listado/estilos).

### User Story 2

- Ejecutar en paralelo T028 y T029 (pruebas unitarias y E2E de solo lectura).

### User Story 3

- Ejecutar en paralelo T035, T036, T037 y T038 (cambio de rol, sesión inválida y recarga consistente).

---

## Implementation Strategy

### MVP First (US1 only)

1. Completar Phase 1 y Phase 2.
2. Completar US1 (T014-T027).
3. Validar criterio independiente de US1.
4. Demostrar CRUD admin funcional.

### Incremental Delivery

1. Entregar MVP con US1.
2. Añadir US2 para modo lectura seguro de empleado.
3. Añadir US3 para consistencia entre sesiones.
4. Cerrar con Phase 6 (contratos, quickstart, documentación).

### Parallel Team Strategy

1. Equipo completo en Setup + Foundational.
2. Luego reparto por historias:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integración final y polish.
