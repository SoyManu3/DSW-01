# Tasks: CRUD de empleados

**Input**: Design documents from `/specs/001-crud-empleados/`  
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: No se generan tareas de pruebas explícitas porque no fueron solicitadas de forma explícita en la especificación; las validaciones funcionales se cubren en criterios independientes por historia.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicialización del proyecto Spring Boot y estructura base.

- [x] T001 Inicializar proyecto Spring Boot 3 con Java 17 y dependencias base en pom.xml
- [x] T002 Crear clase principal de arranque en src/main/java/com/example/empleados/EmpleadosApplication.java
- [x] T003 [P] Crear estructura de paquetes config/controller/domain/repository/service en src/main/java/com/example/empleados/
- [x] T004 [P] Configurar propiedades base de la aplicación en src/main/resources/application.yml
- [x] T005 [P] Definir variables de entorno de ejemplo para datasource en .env.example

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Infraestructura transversal obligatoria antes de cualquier historia.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T006 Crear configuración Docker Compose de PostgreSQL en docker/docker-compose.yml
- [x] T007 Configurar seguridad Basic Auth y rutas públicas de Swagger en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T007A Definir prefijo versionado `/api/v1` para rutas públicas del módulo en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T008 [P] Implementar entidad JPA Empleado con PK compuesta (`prefijo`, `numeroAutonumerico`) y clave externa derivada `EMP-<n>` en src/main/java/com/example/empleados/domain/Empleado.java
- [x] T009 [P] Crear repositorio JPA de empleados en src/main/java/com/example/empleados/repository/EmpleadoRepository.java
- [x] T010 [P] Crear DTO de alta sin campo `clave`, con validaciones Bean Validation, en src/main/java/com/example/empleados/controller/dto/EmpleadoCreateRequest.java
- [x] T011 [P] Crear DTO de actualización con restricciones 1..100 en src/main/java/com/example/empleados/controller/dto/EmpleadoUpdateRequest.java
- [x] T012 [P] Crear DTO de respuesta de empleado en src/main/java/com/example/empleados/controller/dto/EmpleadoResponse.java
- [x] T013 Implementar manejo global de errores HTTP 400/404/409 en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T014 Configurar OpenAPI/Swagger (metadatos y esquema Basic Auth) en src/main/java/com/example/empleados/config/OpenApiConfig.java
- [x] T015 Crear migración inicial con PK compuesta (`prefijo`, `numero_autonumerico`) y unicidad de clave derivada en src/main/resources/db/migration/V1\_\_create_empleados_table.sql

**Checkpoint**: Fundación completa; historias de usuario pueden implementarse.

---

## Phase 3: User Story 1 - Registrar empleado (Priority: P1) 🎯 MVP

**Goal**: Permitir alta de empleados con clave autogenerada `EMP-<n>` y validación de longitudes.

**Independent Test**: Crear un empleado válido, verificar respuesta exitosa y confirmar generación de clave `EMP-<n>` sin duplicados y rechazo de campos > 100.

### Implementation for User Story 1

- [x] T016 [US1] Implementar excepciones de dominio para conflictos de generación de clave y validación en src/main/java/com/example/empleados/service/exception/EmpleadoConflictException.java
- [x] T017 [US1] Implementar servicio de creación con generación de clave `EMP-<n>` a partir de PK compuesta en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T018 [US1] Implementar endpoint POST `/api/v1/empleados` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T019 [US1] Mapear entidad↔DTO para flujo de creación en src/main/java/com/example/empleados/service/mapper/EmpleadoMapper.java
- [x] T020 [US1] Alinear respuesta `201/400/409` del endpoint de creación con el contrato y exponer clave generada `EMP-<n>` en specs/001-crud-empleados/contracts/empleados.openapi.yaml

**Checkpoint**: User Story 1 funcional de forma independiente.

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Permitir consulta por clave y listado paginado de empleados (10 por consulta).

**Independent Test**: Obtener lista paginada con tamaño 10 y recuperar por clave existente/no existente con códigos HTTP correctos.

### Implementation for User Story 2

- [x] T021 [US2] Implementar método de listado paginado (10 por consulta) en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T022 [US2] Implementar método de consulta por clave con not found en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T023 [US2] Implementar endpoint GET `/api/v1/empleados?page={n}` con respuesta paginada en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T024 [US2] Implementar endpoint GET `/api/v1/empleados/{clave}` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T025 [US2] Alinear respuestas `200/404/401` de consultas con versionado y paginación en specs/001-crud-empleados/contracts/empleados.openapi.yaml

**Checkpoint**: User Stories 1 y 2 funcionales de forma independiente.

---

## Phase 5: User Story 3 - Actualizar y eliminar empleado (Priority: P3)

**Goal**: Permitir actualización de campos editables y eliminación por clave.

**Independent Test**: Actualizar empleado existente sin permitir cambio de clave derivada `EMP-<n>` y eliminar empleado validando `204/404`.

### Implementation for User Story 3

- [x] T026 [US3] Implementar actualización de nombre/direccion/telefono sin cambio de `prefijo`, `numeroAutonumerico` ni clave derivada en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T027 [US3] Implementar eliminación por clave en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T028 [US3] Implementar endpoint PUT `/api/v1/empleados/{clave}` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T029 [US3] Implementar endpoint DELETE `/api/v1/empleados/{clave}` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T030 [US3] Alinear respuestas `200/204/404/400` de update-delete con el contrato en specs/001-crud-empleados/contracts/empleados.openapi.yaml

**Checkpoint**: Todas las historias de usuario funcionales e independientes.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Consolidación transversal y validación final.

- [x] T031 [P] Actualizar guía de ejecución y verificación final en specs/001-crud-empleados/quickstart.md
- [x] T032 Verificar consistencia de seguridad Basic Auth en todos los endpoints de empleados en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T033 [P] Revisar y ajustar documentación OpenAPI final para reflejar implementación real (rutas `/api/v1` y paginación de 10) en specs/001-crud-empleados/contracts/empleados.openapi.yaml
- [x] T034 Ejecutar validación manual end-to-end del quickstart y registrar resultado en specs/001-crud-empleados/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todas las historias.
- **Phase 3-5 (User Stories)**: dependen de Phase 2.
- **Phase 6 (Polish)**: depende de historias completadas.

### User Story Dependencies

- **US1 (P1)**: inicia tras Foundational; no depende de US2/US3.
- **US2 (P2)**: inicia tras Foundational; puede usar artefactos de US1 pero mantiene prueba independiente.
- **US3 (P3)**: inicia tras Foundational; requiere base CRUD previa y mantiene prueba independiente.

### Within Each User Story

- Servicio antes que endpoint.
- Endpoint antes que ajuste final de contrato.
- Cerrar criterios de prueba independiente antes de pasar de historia.

### Parallel Opportunities

- Setup: T003, T004 y T005 en paralelo.
- Foundational: T008, T009, T010, T011, T012 en paralelo.
- Polish: T031 y T033 en paralelo.

---

## Parallel Example: User Story 1

```bash
# Trabajo paralelo inicial de US1:
Task: "Implementar servicio de creación con generación de clave EMP-<n> a partir de PK compuesta en src/main/java/com/example/empleados/service/EmpleadoService.java"
Task: "Mapear entidad↔DTO para flujo de creación en src/main/java/com/example/empleados/service/mapper/EmpleadoMapper.java"

# Luego integrar endpoint:
Task: "Implementar endpoint POST /api/v1/empleados en src/main/java/com/example/empleados/controller/EmpleadoController.java"
```

---

## Parallel Example: User Story 2

```bash
Task: "Implementar método de listado paginado (10 por consulta) en src/main/java/com/example/empleados/service/EmpleadoService.java"
Task: "Implementar método de consulta por clave con not found en src/main/java/com/example/empleados/service/EmpleadoService.java"
```

---

## Parallel Example: User Story 3

```bash
Task: "Implementar actualización de nombre/direccion/telefono sin cambio de prefijo/numeroAutonumerico ni clave derivada en src/main/java/com/example/empleados/service/EmpleadoService.java"
Task: "Implementar eliminación por clave en src/main/java/com/example/empleados/service/EmpleadoService.java"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup).
2. Completar Phase 2 (Foundational).
3. Completar Phase 3 (US1).
4. Validar criterio independiente de US1.

### Incremental Delivery

1. Setup + Foundational.
2. Entregar US1 (alta).
3. Entregar US2 (consulta).
4. Entregar US3 (actualización/eliminación).
5. Cerrar polish transversal.

### Parallel Team Strategy

1. Equipo completo en Setup y Foundational.
2. Después de Foundational:
   - Dev A: US1
   - Dev B: US2
   - Dev C: US3
3. Integración final y polish.

---

## Notes

- Todos los ítems cumplen formato checklist obligatorio `- [ ] T### [P] [US#] Descripción con ruta`.
- Las tareas `[P]` están marcadas solo cuando no dependen de archivos/entregables incompletos.
- Cada historia mantiene criterio de prueba independiente para demos incrementales.
