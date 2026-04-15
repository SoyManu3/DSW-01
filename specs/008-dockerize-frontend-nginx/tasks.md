# Tasks: Contenedor Docker Frontend con Nginx

**Input**: Design documents from `/specs/008-dockerize-frontend-nginx/`  
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar artefactos base de contenedorización frontend.

- [x] T001 [US1] Crear ignore de build/contexto Docker frontend en `frontend/.dockerignore`
- [x] T002 [US1] Crear configuración base Nginx con fallback SPA en `frontend/nginx/default.conf`
- [x] T003 [P] [US1] Crear plantilla de configuración runtime para API base en `frontend/nginx/env.template.js`
- [x] T004 [P] [US1] Crear script de arranque Nginx para materializar variables de entorno en `frontend/nginx/docker-entrypoint.sh`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura bloqueante para imagen, configuración runtime y servicio Compose.

**⚠️ CRITICAL**: Ninguna historia de usuario inicia hasta completar esta fase.

- [x] T005 [US1] Implementar imagen multi-stage Angular+Nginx en `frontend/Dockerfile`
- [x] T006 [P] [US1] Incluir carga de configuración runtime en HTML base en `frontend/src/index.html`
- [x] T007 [P] [US1] Definir configuración runtime por defecto para modo local fuera de Docker en `frontend/public/runtime-config.js`
- [x] T008 [US1] Implementar resolución de `API_BASE_URL` con fallback en `frontend/src/environments/environment.ts`
- [x] T009 [P] [US1] Alinear fallback de entorno de desarrollo en `frontend/src/environments/environment.development.ts`
- [x] T010 [US1] Integrar servicio `frontend` (build, puertos `4200:80`, env, depends_on, healthcheck) en `docker/docker-compose.yml`

**Checkpoint**: Fundación lista; historias de usuario pueden ejecutarse en paralelo.

---

## Phase 3: User Story 1 - Ejecutar frontend en contenedor (Priority: P1) 🎯 MVP

**Goal**: Levantar frontend en contenedor Nginx accesible por `http://localhost:4200` sin depender de Node local.

**Independent Test**: Levantar solo el servicio frontend con Compose y comprobar respuesta HTTP exitosa y carga inicial de la app.

### Tests for User Story 1

- [x] T011 [P] [US1] Crear prueba automatizada smoke de frontend (`build + up + curl /`) en `docker/tests/frontend-smoke.sh`

### Implementation for User Story 1

- [x] T012 [US1] Ajustar copia de artefactos Angular y comando final de arranque Nginx en `frontend/Dockerfile`
- [x] T013 [P] [US1] Configurar rutas SPA con `try_files` y entrega de `runtime-config.js` en `frontend/nginx/default.conf`
- [x] T014 [US1] Implementar reemplazo de `API_BASE_URL` en arranque de contenedor en `frontend/nginx/docker-entrypoint.sh`
- [x] T015 [US1] Documentar flujo de validación independiente de frontend-only en `specs/008-dockerize-frontend-nginx/quickstart.md`

**Checkpoint**: US1 funcional e independiente (MVP).

---

## Phase 4: User Story 2 - Integración con stack local (Priority: P2)

**Goal**: Ejecutar frontend contenedorizado junto a `api` y `postgres` en un único `docker-compose`.

**Independent Test**: Levantar stack completo y verificar que `frontend`, `api` y `postgres` quedan operativos sin conflictos de puertos/red.

### Tests for User Story 2

- [x] T016 [P] [US2] Crear prueba automatizada de integración del stack completo (`frontend+api+postgres`) en `docker/tests/frontend-stack-integration.sh`

### Implementation for User Story 2

- [x] T017 [US2] Ajustar dependencias de arranque del servicio frontend respecto de `api` en `docker/docker-compose.yml`
- [x] T018 [US2] Configurar `API_BASE_URL` por defecto para ejecución en stack Compose en `docker/docker-compose.yml`
- [x] T019 [P] [US2] Actualizar contrato operativo de integración de servicio frontend en `specs/008-dockerize-frontend-nginx/contracts/frontend-compose-service-contract.md`
- [x] T020 [US2] Actualizar quickstart de arranque/apagado del stack completo en `specs/008-dockerize-frontend-nginx/quickstart.md`

**Checkpoint**: US2 funcional e independiente.

---

## Phase 5: User Story 3 - Operación consistente entre entornos (Priority: P3)

**Goal**: Garantizar una operación reproducible del frontend contenedorizado con configuración y troubleshooting explícitos.

**Independent Test**: Ejecutar quickstart en máquina limpia y confirmar que el frontend arranca con pasos documentados sin ajustes adicionales.

### Tests for User Story 3

- [x] T021 [P] [US3] Crear prueba automatizada de configuración runtime (`API_BASE_URL` default vs override) en `docker/tests/frontend-runtime-config.sh`

### Implementation for User Story 3

- [x] T022 [US3] Documentar precedencia de configuración runtime (`API_BASE_URL` explícita vs default) en `specs/008-dockerize-frontend-nginx/contracts/frontend-runtime-config-contract.md`
- [x] T023 [P] [US3] Documentar manejo de edge cases (puerto ocupado, build fallido, healthcheck no saludable) en `specs/008-dockerize-frontend-nginx/quickstart.md`
- [x] T024 [P] [US3] Consolidar decisiones finales y tradeoffs operativos en `specs/008-dockerize-frontend-nginx/research.md`
- [x] T025 [US3] Sincronizar README frontend con el flujo Docker+Nginx aprobado en `frontend/README.md`

**Checkpoint**: US3 funcional e independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre de consistencia documental y validación transversal contra criterios de éxito.

- [x] T026 [P] [US3] Crear runner unificado de pruebas automatizadas de contenedor en `docker/tests/run-frontend-container-tests.sh`
- [x] T027 [P] [US3] Verificar consistencia de puerto y healthcheck entre spec/plan/contratos en `specs/008-dockerize-frontend-nginx/spec.md`
- [x] T028 [P] [US3] Verificar consistencia de `API_BASE_URL` default y versión `/api/v1` entre contratos y compose en `specs/008-dockerize-frontend-nginx/contracts/frontend-runtime-config-contract.md`
- [x] T029 [P] [US3] Validar estructura final de tareas y evidencias operativas en `specs/008-dockerize-frontend-nginx/checklists/requirements.md`
- [ ] T030 [US3] Ejecutar y registrar validación de quickstart end-to-end en `specs/008-dockerize-frontend-nginx/quickstart.md`
- [x] T031 [P] [US3] Verificar disponibilidad de Swagger/OpenAPI con el stack levantado y registrar evidencia en `specs/008-dockerize-frontend-nginx/quickstart.md`
- [x] T032 [P] [US3] Verificar no-regresión de paginación de 10 elementos en endpoints de colección (`size=10`) y registrar evidencia en `specs/008-dockerize-frontend-nginx/quickstart.md`
- [x] T033 [P] [US3] Verificar preservación de seguridad HTTP Basic Auth (sin credenciales debe fallar, con credenciales válidas debe responder) y registrar evidencia en `specs/008-dockerize-frontend-nginx/quickstart.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias, puede iniciar inmediatamente.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3-5 (User Stories)**: dependen de completar Phase 2.
- **Phase 6 (Polish)**: depende de historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; entrega MVP desplegable.
- **US2 (P2)**: inicia tras Foundational; recomienda partir de artefactos de US1 para integración completa.
- **US3 (P3)**: inicia tras Foundational; recomienda partir de US1+US2 para documentar operación final real.

### Story Completion Order

1. US1 (MVP)
2. US2
3. US3

---

## Parallel Execution Examples

### User Story 1

- Ejecutar en paralelo T011 y T013 (prueba smoke + configuración Nginx en archivos distintos).

### User Story 2

- Ejecutar en paralelo T016 y T019 (prueba de integración + contrato de servicio).

### User Story 3

- Ejecutar en paralelo T021, T023 y T024 (prueba runtime + quickstart + research).

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1.
2. Completar Phase 2 (bloqueante).
3. Completar Phase 3 (US1).
4. Ejecutar T011 y validar frontend en `4200`.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (MVP).
3. Agregar US2 (stack completo).
4. Agregar US3 (consistencia y onboarding).
5. Cerrar con Polish.

### Parallel Team Strategy

1. Equipo completo en Phase 1-2.
2. Luego reparto por historias:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integración final en Phase 6.

---

## Notes

- Se agregaron pruebas automatizadas proporcionales para cumplir el principio constitucional de calidad ejecutable.
- Todas las tareas mantienen formato checklist estricto con ID secuencial y ruta de archivo explícita.
- La ejecución de validación quickstart end-to-end de stack Docker (T030) quedó pendiente en este entorno por permisos sobre `/var/run/docker.sock`.
- Las validaciones de OpenAPI, paginación fija (size=10) y preservación de HTTP Basic Auth (T031-T033) se ejecutaron contra API local activa en `http://localhost:8080` y se registraron en quickstart.
