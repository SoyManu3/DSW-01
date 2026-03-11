# Tasks: Paginación de consultas de empleados

**Input**: Design documents from `/specs/001-pagination-10-items/`  
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: No se incluyen tareas de tests explícitas porque la especificación no solicita enfoque TDD ni tests-first para esta feature.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar el alcance técnico y documentación base de la feature.

- [x] T001 Verificar y consolidar decisiones de diseño en specs/001-pagination-10-items/plan.md
- [x] T002 Actualizar criterios de validación manual de paginación en specs/001-pagination-10-items/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Base común obligatoria antes de implementar historias de usuario.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T003 Definir DTO de sobre paginado en src/main/java/com/example/empleados/controller/dto/PaginatedEmpleadosResponse.java
- [x] T004 Ajustar versión de ruta base del controlador a `/api/v1/empleados` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T005 Definir contrato base de listado paginado en specs/001-pagination-10-items/contracts/empleados-pagination.openapi.yaml

**Checkpoint**: Fundación lista; implementación de historias puede avanzar.

---

## Phase 3: User Story 1 - Listar empleados paginados (Priority: P1) 🎯 MVP

**Goal**: Exponer listado paginado con tamaño fijo 10 y metadatos de navegación.

**Independent Test**: Invocar `GET /api/v1/empleados?page=0` con credenciales válidas y validar `size=10`, `content<=10`, `page`, `totalElements`, `totalPages`.

### Implementation for User Story 1

- [x] T006 [US1] Implementar método `listar(page)` con `PageRequest` tamaño fijo 10 en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T007 [US1] Normalizar `page` negativa a `0` en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T008 [US1] Implementar endpoint `GET /api/v1/empleados?page=` devolviendo `PaginatedEmpleadosResponse` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T009 [P] [US1] Asegurar orden determinista por `id.numeroAutonumerico` ascendente en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T010 [US1] Actualizar ejemplo de respuesta y validación de `size=10` en specs/001-pagination-10-items/quickstart.md

**Checkpoint**: User Story 1 funcional e independientemente demostrable.

---

## Phase 4: User Story 2 - Compatibilidad de seguridad y contrato (Priority: P2)

**Goal**: Mantener protección Basic Auth y trazabilidad OpenAPI del listado paginado versionado.

**Independent Test**: `GET /api/v1/empleados?page=0` sin credenciales retorna `401`; con credenciales válidas retorna `200` y coincide con contrato OpenAPI.

### Implementation for User Story 2

- [x] T011 [US2] Actualizar protección de rutas versionadas `/api/v1/empleados/**` en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T012 [US2] Documentar parámetros y esquema paginado (`content`, `page`, `size`, `totalElements`, `totalPages`) en specs/001-pagination-10-items/contracts/empleados-pagination.openapi.yaml
- [x] T013 [P] [US2] Sincronizar colección de pruebas manuales con `/api/v1/empleados?page=0` en postman/empleados.postman_collection.json
- [x] T014 [US2] Alinear guía de consumo con autenticación y rutas versionadas en specs/001-pagination-10-items/quickstart.md

**Checkpoint**: User Stories 1 y 2 completos, seguros y documentados.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal y verificación final.

- [x] T015 [P] Verificar consistencia entre spec y plan para requisitos FR-001..FR-006 en specs/001-pagination-10-items/spec.md
- [x] T016 Validar compilación y pruebas del módulo con cambios de paginación en src/test/java/com/example/empleados/unit/EmpleadoServiceTest.java
- [x] T017 [P] Confirmar que quickstart y contrato describen exactamente tamaño fijo 10 en specs/001-pagination-10-items/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: inicia inmediatamente.
- **Foundational (Phase 2)**: depende de Setup y bloquea historias.
- **User Stories (Phase 3+)**: dependen de Foundational.
- **Polish (Phase 5)**: depende de historias implementadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; entrega MVP funcional.
- **US2 (P2)**: inicia tras Foundational; depende de endpoints versionados y paginados definidos en US1.

### Within Each User Story

- Servicio antes de controlador para asegurar contrato interno.
- Controlador antes de documentación final.
- Documentación final antes de validación de cierre.

### Parallel Opportunities

- `T009` puede ejecutarse en paralelo con `T010` dentro de US1.
- `T013` puede ejecutarse en paralelo con `T012` dentro de US2.
- `T015` y `T017` pueden ejecutarse en paralelo en fase de Polish.

---

## Parallel Example: User Story 1

```bash
Task: "Asegurar orden determinista por id.numeroAutonumerico ascendente en src/main/java/com/example/empleados/service/EmpleadoService.java"
Task: "Actualizar ejemplo de respuesta y validación de size=10 en specs/001-pagination-10-items/quickstart.md"
```

---

## Parallel Example: User Story 2

```bash
Task: "Documentar parámetros y esquema paginado en specs/001-pagination-10-items/contracts/empleados-pagination.openapi.yaml"
Task: "Sincronizar colección de pruebas manuales con /api/v1/empleados?page=0 en postman/empleados.postman_collection.json"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Setup y Foundational.
2. Completar US1.
3. Validar endpoint paginado de forma independiente.

### Incremental Delivery

1. Entregar US1 (paginación funcional).
2. Entregar US2 (seguridad + contrato + documentación).
3. Ejecutar Polish y validación final.

### Parallel Team Strategy

1. Equipo completo en Foundational.
2. Luego:
   - Dev A: US1 (`EmpleadoService`, `EmpleadoController`)
   - Dev B: US2 (SecurityConfig, contrato OpenAPI, Postman)
3. Convergencia en fase de Polish.

---

## Notes

- Todas las tareas cumplen formato obligatorio `- [ ] T### [P] [US#] Descripción con ruta`.
- Las tareas marcadas `[P]` están limitadas a archivos sin dependencia directa entre sí.
- Cada historia mantiene criterio de prueba independiente para entrega incremental.
