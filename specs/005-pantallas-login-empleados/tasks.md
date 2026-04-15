# Tasks: Pantallas de Login de Empleados

**Input**: Design documents from `/specs/005-pantallas-login-empleados/`  
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/login-ui-contract.md, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la especificación exige Cypress E2E para esta feature.  
**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicializar frontend Angular 21 LTS y tooling base para desarrollo.

- [X] T001 Crear proyecto Angular 21 LTS en `frontend/` con configuración inicial en frontend/angular.json
- [X] T002 Configurar dependencias base de frontend y scripts npm en frontend/package.json
- [X] T003 [P] Configurar TypeScript y compilación Angular en frontend/tsconfig.json
- [X] T004 [P] Configurar estilos globales y tokens visuales base en frontend/src/styles.css
- [X] T005 Configurar Cypress en frontend/cypress.config.ts
- [X] T006 [P] Crear estructura base de carpetas de feature login en frontend/src/app/pages/login/.gitkeep
- [X] T007 [P] Crear estructura base de rutas protegidas en frontend/src/app/pages/inicio/.gitkeep
- [X] T008 [P] Configurar variables de entorno para URL backend en frontend/src/environments/environment.development.ts

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura común que bloquea la implementación de todas las historias.

**⚠️ CRITICAL**: Ninguna historia puede comenzar antes de completar esta fase.

- [X] T009 Definir modelo `CredencialAccesoUI` y tipos de estado de autenticación en frontend/src/app/services/auth/auth.models.ts
- [X] T010 Implementar utilitario de codificación Basic Auth en frontend/src/app/services/auth/basic-auth.encoder.ts
- [X] T011 Implementar servicio base de autenticación alineado a API en frontend/src/app/services/auth/auth.service.ts
- [X] T012 Implementar interceptor para cabecera `Authorization` Basic en frontend/src/app/interceptors/basic-auth.interceptor.ts
- [X] T013 Implementar guard de ruta protegida `/inicio` en frontend/src/app/guards/auth.guard.ts
- [X] T014 Configurar routing principal con `/login` y `/inicio` en frontend/src/app/app.routes.ts
- [X] T015 Configurar bootstrap de providers (router/interceptor/http) en frontend/src/main.ts
- [X] T016 Implementar manejo global de error técnico de autenticación en frontend/src/app/services/auth/auth-error.mapper.ts
- [X] T044 Verificar provisión local de PostgreSQL vía Docker Compose para ejecución E2E en docker/docker-compose.yml
- [X] T045 Verificar trazabilidad de endpoints usados por login con contrato OpenAPI/Swagger en specs/003-autenticacion-empleados/contracts/empleados-auth.openapi.yaml
- [X] T046 Verificar que las llamadas del frontend para autenticación de recursos protegidos usan rutas versionadas `/api/v1/...` en frontend/src/app/services/auth/auth.service.ts

**Checkpoint**: Base técnica lista para construir historias en paralelo.

---

## Phase 3: User Story 1 - Acceso visual al sistema (Priority: P1) 🎯 MVP

**Goal**: Entregar pantalla de login funcional con email + contraseña, autenticación y redirección a `/inicio`.

**Independent Test**: Un empleado puede autenticarse con credenciales válidas y navegar a `/inicio`; con credenciales inválidas recibe `Credenciales inválidas`.

### Tests for User Story 1

- [X] T017 [P] [US1] Crear spec Cypress de login exitoso en frontend/cypress/e2e/login/login-success.cy.ts
- [X] T018 [P] [US1] Crear spec Cypress de credenciales inválidas con mensaje genérico en frontend/cypress/e2e/login/login-invalid-credentials.cy.ts

### Implementation for User Story 1

- [X] T019 [US1] Implementar componente de página login en frontend/src/app/pages/login/login.page.ts
- [X] T020 [US1] Implementar plantilla de pantalla login (email, contraseña, CTA) en frontend/src/app/pages/login/login.page.html
- [X] T021 [US1] Implementar estilos de pantalla login en frontend/src/app/pages/login/login.page.css
- [X] T022 [US1] Implementar flujo submit -> autenticación -> redirección `/inicio` en frontend/src/app/pages/login/login.page.ts
- [X] T023 [US1] Implementar página protegida de destino `/inicio` en frontend/src/app/pages/inicio/inicio.page.ts
- [X] T024 [US1] Implementar plantilla y estado mínimo de `/inicio` en frontend/src/app/pages/inicio/inicio.page.html

**Checkpoint**: US1 funcional y demostrable de forma independiente.

---

## Phase 4: User Story 2 - Validación y estados de formulario (Priority: P2)

**Goal**: Entregar validaciones por campo, estado de envío, bloqueo de reenvío y feedback visual consistente.

**Independent Test**: Con campos inválidos no se envía el formulario; al corregir entradas, el estado cambia correctamente y permite reintento.

### Tests for User Story 2

- [X] T025 [P] [US2] Crear spec Cypress de validación de campos obligatorios en frontend/cypress/e2e/login/login-validation-required.cy.ts
- [X] T026 [P] [US2] Crear spec Cypress de bloqueo de reenvío durante estado `ENVIANDO` en frontend/cypress/e2e/login/login-prevent-double-submit.cy.ts
- [X] T027 [P] [US2] Crear spec Cypress de recuperación tras error técnico en frontend/cypress/e2e/login/login-technical-error-retry.cy.ts

### Implementation for User Story 2

- [X] T028 [US2] Implementar validaciones reactivas de email y contraseña en frontend/src/app/pages/login/login.page.ts
- [X] T029 [US2] Implementar mensajes de error por campo en frontend/src/app/pages/login/login.page.html
- [X] T030 [US2] Implementar estado visual `ENVIANDO` y bloqueo de botón en frontend/src/app/pages/login/login.page.html
- [X] T031 [US2] Implementar manejo de estado `ERROR_TECNICO` con opción de reintento en frontend/src/app/pages/login/login.page.ts
- [X] T032 [US2] Ajustar consistencia visual de estados (`INICIAL`, `VALIDACION_ERROR`, `ENVIANDO`, `RECHAZADO`) en frontend/src/app/pages/login/login.page.css

**Checkpoint**: US1 y US2 funcionales e independientes.

---

## Phase 5: User Story 3 - Cobertura E2E del flujo visual (Priority: P3)

**Goal**: Consolidar suite E2E de Cypress contra backend real local y cubrir accesibilidad base requerida.

**Independent Test**: La suite de login corre localmente contra API real y valida éxito, error, validación y navegación por teclado con foco visible.

### Tests for User Story 3

- [X] T033 [P] [US3] Crear comando/utilidad Cypress para credenciales de prueba en frontend/cypress/support/commands.ts
- [X] T034 [P] [US3] Crear spec Cypress de accesibilidad por teclado y foco visible en frontend/cypress/e2e/login/login-accessibility-keyboard.cy.ts
- [X] T035 [P] [US3] Crear spec Cypress de guard + redirección cuando ya está autenticado en frontend/cypress/e2e/login/login-already-authenticated.cy.ts

### Implementation for User Story 3

- [X] T036 [US3] Configurar `baseUrl` y variables para ejecución contra backend local real en frontend/cypress.config.ts
- [X] T037 [US3] Configurar soporte global Cypress (hooks y utilidades) en frontend/cypress/support/e2e.ts
- [X] T038 [US3] Documentar dataset/credenciales de prueba para E2E en frontend/cypress/fixtures/login-users.json
- [X] T039 [US3] Integrar script de ejecución E2E en frontend/package.json

**Checkpoint**: Todas las historias cubiertas con ejecución E2E repetible.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Ajustes finales de calidad, documentación y validación transversal.

- [X] T040 [P] Actualizar guía de uso frontend y login en frontend/README.md
- [X] T041 [P] Verificar quickstart y ajustar pasos de ejecución reales en specs/005-pantallas-login-empleados/quickstart.md
- [X] T042 Validar trazabilidad con contrato de UI y alinear criterios finales en specs/005-pantallas-login-empleados/contracts/login-ui-contract.md
- [X] T043 Ejecutar suite Cypress completa y registrar evidencia de resultados en specs/005-pantallas-login-empleados/checklists/requirements.md
- [X] T047 Medir y registrar p95 del tiempo de login exitoso (30 corridas E2E) para validar SC-001 en specs/005-pantallas-login-empleados/checklists/requirements.md
- [X] T048 Diseñar y ejecutar encuesta breve de claridad del mensaje de error (mínimo 10 usuarios) para validar SC-004 en specs/005-pantallas-login-empleados/checklists/requirements.md
- [X] T049 Verificar consistencia visual de estados de login contra FR-007 y registrar evidencia en specs/005-pantallas-login-empleados/contracts/login-ui-contract.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: inicia inmediatamente.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3 (US1)**: depende de Phase 2.
- **Phase 4 (US2)**: depende de Phase 2; puede avanzar en paralelo con US1 si hay capacidad.
- **Phase 5 (US3)**: depende de Phase 2 y de artefactos mínimos de US1/US2 para cobertura completa.
- **Phase 6 (Polish)**: depende de completar historias objetivo.

### User Story Dependencies

- **US1 (P1)**: independiente tras Foundational; define el MVP.
- **US2 (P2)**: independiente tras Foundational; extiende validación/estados de login.
- **US3 (P3)**: depende funcionalmente de US1/US2 para validar todos los escenarios E2E.

### Within Each User Story

- Crear pruebas E2E primero (deben fallar inicialmente).
- Implementar componentes/servicios después.
- Completar integración de historia antes de pasar a la siguiente prioridad.

### Parallel Opportunities

- En Setup: T003, T004, T006, T007, T008 pueden ejecutarse en paralelo.
- En Foundational: T012 y T013 pueden avanzar en paralelo tras T011.
- En Foundational: T045 y T046 pueden avanzar en paralelo tras T011.
- En US1: T017 y T018 en paralelo.
- En US2: T025, T026, T027 en paralelo.
- En US3: T033, T034, T035 en paralelo.
- En Polish: T040 y T041 en paralelo.

---

## Parallel Example: User Story 1

```bash
Task: T017 [US1] frontend/cypress/e2e/login/login-success.cy.ts
Task: T018 [US1] frontend/cypress/e2e/login/login-invalid-credentials.cy.ts
```

## Parallel Example: User Story 2

```bash
Task: T025 [US2] frontend/cypress/e2e/login/login-validation-required.cy.ts
Task: T026 [US2] frontend/cypress/e2e/login/login-prevent-double-submit.cy.ts
Task: T027 [US2] frontend/cypress/e2e/login/login-technical-error-retry.cy.ts
```

## Parallel Example: User Story 3

```bash
Task: T033 [US3] frontend/cypress/support/commands.ts
Task: T034 [US3] frontend/cypress/e2e/login/login-accessibility-keyboard.cy.ts
Task: T035 [US3] frontend/cypress/e2e/login/login-already-authenticated.cy.ts
```

---

## Implementation Strategy

### MVP First (US1)

1. Completar Setup (Phase 1).
2. Completar Foundational (Phase 2).
3. Completar US1 (Phase 3).
4. Validar de forma independiente flujo login -> `/inicio`.

### Incremental Delivery

1. MVP con US1.
2. Añadir US2 para robustecer validación y estados.
3. Añadir US3 para cobertura E2E integral y calidad continua.
4. Cerrar con Polish transversal.

### Parallel Team Strategy

1. Equipo completo en Phase 1 y 2.
2. Luego dividir:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3 (cuando existan flujos mínimos de US1/US2)
3. Integrar y validar en Phase 6.

---

## Notes

- Todas las tareas mantienen formato checklist estricto con ID secuencial.
- Las tareas con `[P]` no comparten archivos ni dependen de incompletas en la misma fase.
- Las tareas de historia incluyen etiqueta `[US1]`, `[US2]` o `[US3]`.
- Cada historia mantiene criterio de prueba independiente alineado a `spec.md`.
