# Tasks: Autenticación de empleados

**Input**: Design documents from `/specs/003-autenticacion-empleados/`  
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/, quickstart.md

**Tests**: Se incluyen tareas de pruebas porque la especificación exige validación independiente por historia y la constitución requiere cobertura ejecutable para seguridad, datos y contrato HTTP.

**Organization**: Las tareas están agrupadas por historia de usuario para implementación y validación independiente.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Preparar estructura base de pruebas e infraestructura local para la feature.

- [x] T001 Crear paquete de integración en src/test/java/com/example/empleados/integration
- [x] T002 Crear clase base Testcontainers en src/test/java/com/example/empleados/integration/IntegrationTestBase.java
- [x] T003 [P] Crear utilidades de Basic Auth para tests en src/test/java/com/example/empleados/integration/BasicAuthTestUtils.java
- [x] T004 [P] Verificar definición de servicio PostgreSQL local en docker/docker-compose.yml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Construir cimientos técnicos que bloquean todas las historias.

**⚠️ CRITICAL**: No iniciar US1/US2/US3 hasta completar esta fase.

- [x] T005 Crear migración de credenciales en src/main/resources/db/migration/V2\_\_add_auth_fields_to_empleados.sql
- [x] T006 [P] Crear migración de auditoría de autenticación en src/main/resources/db/migration/V3\_\_create_evento_autenticacion_table.sql
- [x] T007 [P] Extender entidad Empleado con campos de acceso en src/main/java/com/example/empleados/domain/Empleado.java
- [x] T008 [P] Crear entidad de auditoría en src/main/java/com/example/empleados/domain/EventoAutenticacion.java
- [x] T009 [P] Ampliar repositorio de empleados por correo en src/main/java/com/example/empleados/repository/EmpleadoRepository.java
- [x] T010 [P] Crear repositorio de eventos de autenticación en src/main/java/com/example/empleados/repository/EventoAutenticacionRepository.java
- [x] T011 Implementar UserDetailsService por correo en src/main/java/com/example/empleados/config/EmpleadoUserDetailsService.java
- [x] T012 Configurar SecurityFilterChain con Basic Auth en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T013 [P] Crear exceptions de autenticación en src/main/java/com/example/empleados/service/exception/EmpleadoBlockedException.java
- [x] T014 [P] Crear exception de credenciales inválidas en src/main/java/com/example/empleados/service/exception/InvalidCredentialsException.java
- [x] T015 Ajustar payload de error con path en src/main/java/com/example/empleados/config/ApiError.java
- [x] T016 Implementar manejo global de 400/401/409/423 en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T017 [P] Implementar entry point HTTP para 401 y 423 en src/main/java/com/example/empleados/config/AuthenticationStatusEntryPoint.java
- [x] T018 [P] Actualizar metadatos OpenAPI para seguridad en src/main/java/com/example/empleados/config/OpenApiConfig.java
- [x] T019 [P] Alinear contrato OpenAPI de autenticación en specs/003-autenticacion-empleados/contracts/empleados-auth.openapi.yaml
- [x] T020 Definir logging estructurado mínimo para errores/auth en src/main/resources/application.yml

**Checkpoint**: Base de seguridad, persistencia y contrato lista para historias independientes.

---

## Phase 3: User Story 1 - Iniciar sesión como empleado (Priority: P1) 🎯 MVP

**Goal**: Permitir autenticación por correo/contraseña con política de contraseña y bloqueo temporal.

**Independent Test**: Autenticación válida accede a recurso protegido; credenciales inválidas rechazan acceso; tras 5 fallos consecutivos se bloquea 15 minutos y luego se desbloquea automáticamente.

### Tests for User Story 1

- [x] T021 [P] [US1] Crear prueba unitaria de política de contraseña en src/test/java/com/example/empleados/unit/PasswordPolicyTest.java
- [x] T022 [P] [US1] Crear prueba unitaria de contador/bloqueo/desbloqueo en src/test/java/com/example/empleados/unit/EmpleadoAuthPolicyServiceTest.java
- [x] T023 [P] [US1] Crear prueba de integración de login implícito Basic Auth en src/test/java/com/example/empleados/integration/EmpleadoAuthenticationIntegrationTest.java

### Implementation for User Story 1

- [x] T024 [US1] Implementar reglas de password y lockout en src/main/java/com/example/empleados/service/EmpleadoAuthPolicyService.java
- [x] T025 [US1] Implementar auditoría de intentos en src/main/java/com/example/empleados/service/AuthenticationAuditService.java
- [x] T026 [US1] Registrar eventos auth success/failure en src/main/java/com/example/empleados/config/AuthenticationEventsListener.java
- [x] T027 [US1] Validar correo/password requeridos en alta en src/main/java/com/example/empleados/controller/dto/EmpleadoCreateRequest.java
- [x] T028 [US1] Validar password opcional en actualización en src/main/java/com/example/empleados/controller/dto/EmpleadoUpdateRequest.java
- [x] T029 [US1] Persistir password hash y estado inicial al crear empleado en src/main/java/com/example/empleados/service/EmpleadoService.java

**Checkpoint**: US1 operativa como MVP (autenticación + bloqueo + desbloqueo).

---

## Phase 4: User Story 2 - Proteger operaciones de empleados (Priority: P2)

**Goal**: Asegurar que CRUD de empleados solo sea accesible para empleados autenticados.

**Independent Test**: Requests sin credenciales o inválidas reciben 401; cuentas bloqueadas reciben 423; autenticados válidos operan endpoints de empleados.

### Tests for User Story 2

- [x] T030 [P] [US2] Crear prueba de integración sin credenciales en src/test/java/com/example/empleados/integration/EmpleadoAuthorizationIntegrationTest.java
- [x] T031 [P] [US2] Crear prueba de integración con credenciales válidas en src/test/java/com/example/empleados/integration/EmpleadoAuthorizationIntegrationTest.java
- [x] T032 [P] [US2] Crear prueba de integración para cuenta bloqueada (423) en src/test/java/com/example/empleados/integration/EmpleadoAuthorizationIntegrationTest.java

### Implementation for User Story 2

- [x] T033 [US2] Aplicar reglas de protección `/api/v1/empleados/**` en src/main/java/com/example/empleados/config/SecurityConfig.java
- [x] T034 [US2] Normalizar respuestas de no autenticado y bloqueado en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T035 [US2] Asegurar semántica de estado en entry point de auth en src/main/java/com/example/empleados/config/AuthenticationStatusEntryPoint.java
- [x] T036 [US2] Actualizar escenarios de validación US2 en specs/003-autenticacion-empleados/quickstart.md

**Checkpoint**: US2 validable sin dependencia de cambios funcionales de US3.

---

## Phase 5: User Story 3 - Gestionar credenciales de acceso (Priority: P3)

**Goal**: Gestionar correo único y rotación de contraseña en alta/actualización de empleados.

**Independent Test**: Alta y actualización con datos válidos funcionan; correo duplicado o contraseña inválida se rechaza.

### Tests for User Story 3

- [x] T037 [P] [US3] Crear prueba de integración de alta con credenciales válidas en src/test/java/com/example/empleados/integration/EmpleadoCredentialManagementIntegrationTest.java
- [x] T038 [P] [US3] Crear prueba de integración de correo duplicado en src/test/java/com/example/empleados/integration/EmpleadoCredentialManagementIntegrationTest.java
- [x] T039 [P] [US3] Crear prueba de integración de rotación de contraseña en src/test/java/com/example/empleados/integration/EmpleadoCredentialManagementIntegrationTest.java

### Implementation for User Story 3

- [x] T040 [US3] Exponer correo y activo en respuesta sin hash en src/main/java/com/example/empleados/controller/dto/EmpleadoResponse.java
- [x] T041 [US3] Ajustar mapper para no filtrar passwordHash en src/main/java/com/example/empleados/service/mapper/EmpleadoMapper.java
- [x] T042 [US3] Aplicar unicidad de correo y rotación de password en src/main/java/com/example/empleados/service/EmpleadoService.java
- [x] T043 [US3] Propagar request/response de credenciales en controlador en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T044 [US3] Reflejar contratos de credenciales en specs/003-autenticacion-empleados/contracts/empleados-auth.openapi.yaml

**Checkpoint**: US3 funcional y demostrable de forma independiente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Cierre transversal de calidad, consistencia contractual y cumplimiento constitucional.

- [x] T045 [P] Ejecutar suite de pruebas unitarias y de integración en src/test/java/com/example/empleados/
- [x] T046 [P] Verificar rutas públicas versionadas `/api/v1` en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T047 [P] Verificar paginación fija de 10 en listados en src/main/java/com/example/empleados/controller/EmpleadoController.java
- [x] T048 [P] Validar consistencia de códigos 400/401/409/423 en src/main/java/com/example/empleados/config/GlobalExceptionHandler.java
- [x] T049 [P] Validar publicación OpenAPI/Swagger en src/main/java/com/example/empleados/config/OpenApiConfig.java
- [x] T050 Ejecutar validación manual de quickstart en specs/003-autenticacion-empleados/quickstart.md
- [x] T051 [P] Verificar contrato de payload `ApiError` (`timestamp`, `status`, `error`, `message`, `path`) para 400/401/409/423 en src/test/java/com/example/empleados/integration/EmpleadoAuthorizationIntegrationTest.java
- [x] T052 [P] Ejecutar validación de rendimiento de autenticación (10 ejecuciones, p95 < 2s) y documentar resultado en specs/003-autenticacion-empleados/quickstart.md

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: sin dependencias.
- **Phase 2 (Foundational)**: depende de Phase 1 y bloquea todo el trabajo de historias.
- **Phase 3 (US1)**: depende de Phase 2.
- **Phase 4 (US2)**: depende de Phase 2.
- **Phase 5 (US3)**: depende de Phase 2.
- **Phase 6 (Polish)**: depende de historias objetivo completadas.

### User Story Dependencies

- **US1 (P1)**: primera entrega MVP tras Foundational.
- **US2 (P2)**: inicia tras Foundational; no depende de US3.
- **US3 (P3)**: inicia tras Foundational; no depende de US2.

### Story Completion Order (Dependency Graph)

`Foundational -> (US1 || US2 || US3) -> Polish`

---

## Parallel Opportunities

- **Setup**: T003, T004.
- **Foundational**: T006, T007, T008, T009, T010, T013, T014, T017, T018, T019.
- **US1**: T021, T022, T023.
- **US2**: T030, T031, T032.
- **US3**: T037, T038, T039.
- **Polish**: T045, T046, T047, T048, T049.

---

## Parallel Example: User Story 1

```bash
# Tests US1 en paralelo
T021 [US1] PasswordPolicyTest
T022 [US1] EmpleadoAuthPolicyServiceTest
T023 [US1] EmpleadoAuthenticationIntegrationTest

# Implementación US1 en paralelo (archivos distintos)
T027 [US1] EmpleadoCreateRequest
T028 [US1] EmpleadoUpdateRequest
T026 [US1] AuthenticationEventsListener
```

## Parallel Example: User Story 2

```bash
# Tests US2 en paralelo
T030 [US2] rechazo sin credenciales
T031 [US2] acceso autenticado
T032 [US2] cuenta bloqueada 423
```

## Parallel Example: User Story 3

```bash
# Tests US3 en paralelo
T037 [US3] alta válida
T038 [US3] correo duplicado
T039 [US3] rotación de contraseña
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 + Phase 2.
2. Completar Phase 3 (US1).
3. Validar prueba independiente de US1.
4. Entregar MVP autenticable.

### Incremental Delivery

1. Foundation (Setup + Foundational).
2. Entrega US1 (autenticación).
3. Entrega US2 (protección de endpoints).
4. Entrega US3 (gestión de credenciales).
5. Cierre transversal (Polish).

### Suggested MVP Scope

- **MVP sugerido**: US1 completo después de Foundational.
- Valor MVP: autenticación por correo/contraseña con lockout y desbloqueo automático.
