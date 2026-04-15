# Tasks: CRUD de Departamentos y Relación con Empleados

**Input**: Design documents from `/specs/004-crud-departamentos/`  
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la constitución y el plan requieren cobertura ejecutable de cambios en seguridad, persistencia y contrato HTTP.

**Organization**: Tareas agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar infraestructura de validación y artefactos base de la feature.

- [x] T001 Crear clase base de integración para departamentos en src/test/java/com/example/empleados/integration/DepartamentosIntegrationTestBase.java
- [x] T002 [P] Crear utilidades Basic Auth para pruebas de departamentos en src/test/java/com/example/empleados/integration/DepartamentosAuthTestUtils.java
- [x] T003 [P] Crear colección Postman de la feature en postman/departamentos.postman_collection.json
- [x] T004 [P] Ajustar guía de arranque de la feature en specs/004-crud-departamentos/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Cimientos técnicos que bloquean todas las historias.

**⚠️ CRITICAL**: No iniciar US1/US2/US3 hasta completar esta fase.

- [x] T005 Crear migración de tabla departamentos en src/main/resources/db/migration/V4\_\_create_departamentos_table.sql
- [x] T006 Crear migración de FK obligatoria en empleados en src/main/resources/db/migration/V5\_\_add_departamento_fk_to_empleados.sql
- [x] T007 [P] Crear entidad JPA Departamento en src/main/java/com/example/empleados/domain/Departamento.java
- [x] T008 [P] Agregar atributo `departamento` obligatorio en src/main/java/com/example/empleados/domain/Empleado.java
- [x] T009 [P] Crear repositorio de departamentos en src/main/java/com/example/empleados/repository/DepartamentoRepository.java
- [x] T010 [P] Añadir consulta por departamento en src/main/java/com/example/empleados/repository/EmpleadoRepository.java
- [x] T011 [P] Crear excepción `DepartamentoDuplicadoException` en src/main/java/com/example/empleados/service/exception/DepartamentoDuplicadoException.java
- [x] T012 [P] Crear excepción `DepartamentoConEmpleadosException` en src/main/java/com/example/empleados/service/exception/DepartamentoConEmpleadosException.java
- [x] T013 Implementar mapeo de errores 400/404/409 en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T014 [P] Crear DTO `DepartamentoCreateRequest` en src/main/java/com/example/empleados/controller/dto/DepartamentoCreateRequest.java
- [x] T015 [P] Crear DTO `DepartamentoUpdateRequest` en src/main/java/com/example/empleados/controller/dto/DepartamentoUpdateRequest.java
- [x] T016 [P] Crear DTO `DepartamentoResponse` en src/main/java/com/example/empleados/controller/dto/DepartamentoResponse.java
- [x] T017 [P] Crear DTO `PaginatedDepartamentosResponse` en src/main/java/com/example/empleados/controller/dto/PaginatedDepartamentosResponse.java
- [x] T018 [P] Crear DTO `EmpleadoDepartamentoResponse` en src/main/java/com/example/empleados/controller/dto/EmpleadoDepartamentoResponse.java
- [x] T019 [P] Crear DTO `PaginatedEmpleadosDepartamentoResponse` en src/main/java/com/example/empleados/controller/dto/PaginatedEmpleadosDepartamentoResponse.java
- [x] T020 Implementar reglas de seguridad para `/api/v1/departamentos/**` en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T021 [P] Actualizar contrato base de la feature en specs/004-crud-departamentos/contracts/departamentos-empleados.openapi.yaml

**Checkpoint**: Fundación lista; historias pueden implementarse independientemente.

---

## Phase 3: User Story 1 - Gestionar departamentos (Priority: P1) 🎯 MVP

**Goal**: Permitir CRUD autenticado de departamentos con validaciones y paginación determinista.

**Independent Test**: CRUD completo de departamentos con Basic Auth, `size=10`, orden `id` ascendente, validación de nombre y unicidad case-sensitive.

### Tests for User Story 1

- [x] T022 [P] [US1] Crear prueba unitaria de validación de nombre en src/test/java/com/example/empleados/unit/DepartamentoValidationTest.java
- [x] T023 [P] [US1] Crear prueba unitaria de unicidad case-sensitive en src/test/java/com/example/empleados/unit/DepartamentoUniquenessPolicyTest.java
- [x] T024 [P] [US1] Crear prueba de integración de CRUD de departamentos en src/test/java/com/example/empleados/integration/DepartamentoCrudIntegrationTest.java
- [x] T025 [P] [US1] Crear prueba de integración de paginación de departamentos en src/test/java/com/example/empleados/integration/DepartamentoPaginationIntegrationTest.java

### Implementation for User Story 1

- [x] T026 [US1] Implementar servicio CRUD de departamentos en src/main/java/com/example/empleados/service/DepartamentoService.java
- [x] T027 [US1] Implementar controlador `/api/v1/departamentos` en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [x] T028 [US1] Aplicar reglas de `nombre` (no vacío, <=100) al create request en src/main/java/com/example/empleados/controller/dto/DepartamentoCreateRequest.java
- [x] T029 [US1] Aplicar reglas de `nombre` (no vacío, <=100) al update request en src/main/java/com/example/empleados/controller/dto/DepartamentoUpdateRequest.java
- [x] T030 [US1] Implementar `empleadosCount` en detalle de departamento en src/main/java/com/example/empleados/controller/dto/DepartamentoResponse.java
- [x] T031 [US1] Implementar paginación fija y orden por id en listado de departamentos en src/main/java/com/example/empleados/service/DepartamentoService.java
- [x] T032 [US1] Alinear OpenAPI de CRUD de departamentos en specs/004-crud-departamentos/contracts/departamentos-empleados.openapi.yaml

**Checkpoint**: US1 funcional y testeable de forma independiente.

---

## Phase 4: User Story 2 - Asignar empleados a un departamento (Priority: P2)

**Goal**: Materializar relación 1:N obligatoria y consulta paginada de empleados por departamento.

**Independent Test**: Empleados se crean/actualizan con departamento obligatorio, detalle muestra contador y endpoint de empleados por departamento responde paginado.

### Tests for User Story 2

- [x] T033 [P] [US2] Crear prueba de integración de asignación empleado-departamento en src/test/java/com/example/empleados/integration/EmpleadoDepartamentoAssignmentIntegrationTest.java
- [x] T034 [P] [US2] Crear prueba de integración de detalle con contador en src/test/java/com/example/empleados/integration/DepartamentoDetailIntegrationTest.java
- [x] T035 [P] [US2] Crear prueba de integración de paginación de empleados por departamento en src/test/java/com/example/empleados/integration/DepartamentoEmpleadosPaginationIntegrationTest.java

### Implementation for User Story 2

- [x] T036 [US2] Añadir `departamentoId` obligatorio al create de empleado en src/main/java/com/example/empleados/controller/dto/EmpleadoCreateRequest.java
- [x] T037 [US2] Añadir `departamentoId` obligatorio al update de empleado en src/main/java/com/example/empleados/controller/dto/EmpleadoUpdateRequest.java
- [x] T038 [US2] Persistir asociación de departamento en alta/actualización de empleado en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T039 [US2] Implementar endpoint `/api/v1/departamentos/{id}/empleados` en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [x] T040 [US2] Implementar paginación fija y orden por id de empleados por departamento en src/main/java/com/example/empleados/service/DepartamentoService.java
- [x] T041 [US2] Alinear OpenAPI de empleados por departamento en specs/004-crud-departamentos/contracts/departamentos-empleados.openapi.yaml

**Checkpoint**: US2 funcional y testeable de forma independiente.

---

## Phase 5: User Story 3 - Proteger consistencia al eliminar departamentos (Priority: P3)

**Goal**: Rechazar eliminación de departamentos con empleados asociados y devolver error consistente.

**Independent Test**: DELETE de departamento con empleados devuelve conflicto y mantiene integridad de datos.

### Tests for User Story 3

- [x] T042 [P] [US3] Crear prueba unitaria de política de borrado en src/test/java/com/example/empleados/unit/DepartamentoDeletionPolicyTest.java
- [x] T043 [P] [US3] Crear prueba de integración de DELETE rechazado en src/test/java/com/example/empleados/integration/DepartamentoDeleteGuardIntegrationTest.java
- [x] T044 [P] [US3] Crear prueba de integración de payload de error consistente en src/test/java/com/example/empleados/integration/DepartamentoErrorPayloadIntegrationTest.java

### Implementation for User Story 3

- [x] T045 [US3] Implementar guarda de borrado por existencia de empleados en src/main/java/com/example/empleados/service/DepartamentoService.java
- [x] T046 [US3] Implementar mensaje de negocio para conflicto de borrado en src/main/java/com/example/empleados/service/exception/DepartamentoConEmpleadosException.java
- [x] T047 [US3] Mapear conflicto de borrado a HTTP 409 consistente en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T048 [US3] Alinear OpenAPI para respuesta 409 de DELETE en specs/004-crud-departamentos/contracts/departamentos-empleados.openapi.yaml

**Checkpoint**: US3 funcional y testeable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Verificación final transversal y cierre de calidad.

- [x] T049 [P] Ejecutar suite completa de pruebas en src/test/java/com/example/empleados/
- [x] T050 [P] Verificar rutas versionadas `/api/v1` en src/main/java/com/example/empleados/controller/DepartamentoController.java
- [x] T051 [P] Verificar paginación fija de 10 en colecciones en src/main/java/com/example/empleados/service/DepartamentoService.java
- [x] T052 [P] Verificar publicación OpenAPI/Swagger en src/main/java/com/example/empleados/config/OpenApiConfig.java
- [x] T053 Ejecutar validación manual de quickstart en specs/004-crud-departamentos/quickstart.md
- [x] T054 [P] Ejecutar medición de p95 < 2s y documentar evidencia en specs/004-crud-departamentos/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: sin dependencias.
- **Foundational (Phase 2)**: depende de Setup y bloquea todas las historias.
- **User Stories (Phase 3+)**: dependen de Foundational; pueden avanzar en paralelo.
- **Polish (Phase 6)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; no depende de otras historias.
- **US2 (P2)**: inicia tras Foundational; no depende de US1 para ser verificable.
- **US3 (P3)**: inicia tras Foundational; no depende de US2 para validarse con fixtures de datos.

### Story Completion Order (Dependency Graph)

`Setup -> Foundational -> (US1 || US2 || US3) -> Polish`

### Within Each User Story

- Tests (siempre) antes de implementación.
- Servicio antes de controlador.
- Implementación antes de alineación contractual final.

---

## Parallel Opportunities

- **Setup**: T002, T003, T004.
- **Foundational**: T007, T008, T009, T010, T011, T012, T014, T015, T016, T017, T018, T019, T021.
- **US1**: T022, T023, T024, T025.
- **US2**: T033, T034, T035.
- **US3**: T042, T043, T044.
- **Polish**: T049, T050, T051, T052, T054.

---

## Parallel Example: User Story 1

```bash
# Tests US1 en paralelo
T022 [US1] DepartamentoValidationTest
T023 [US1] DepartamentoUniquenessPolicyTest
T024 [US1] DepartamentoCrudIntegrationTest
T025 [US1] DepartamentoPaginationIntegrationTest
```

## Parallel Example: User Story 2

```bash
# Tests US2 en paralelo
T033 [US2] EmpleadoDepartamentoAssignmentIntegrationTest
T034 [US2] DepartamentoDetailIntegrationTest
T035 [US2] DepartamentoEmpleadosPaginationIntegrationTest
```

## Parallel Example: User Story 3

```bash
# Tests US3 en paralelo
T042 [US3] DepartamentoDeletionPolicyTest
T043 [US3] DepartamentoDeleteGuardIntegrationTest
T044 [US3] DepartamentoErrorPayloadIntegrationTest
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar criterio independiente de US1.
5. Entregar MVP.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (CRUD departamentos).
3. Entregar US2 (relación + consulta por departamento).
4. Entregar US3 (consistencia de borrado).
5. Cerrar con Polish.

### Parallel Team Strategy

1. Equipo completo en Setup y Foundational.
2. Luego ejecutar en paralelo:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integrar y cerrar en Polish.

---

## Notes

- Todos los ítems usan formato checklist obligatorio.
- Las tareas `[P]` se marcan solo cuando no comparten dependencias incompletas.
- Cada historia conserva prueba independiente y entregable demostrable.
