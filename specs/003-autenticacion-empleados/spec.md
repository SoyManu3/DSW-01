# Feature Specification: Autenticación de empleados

**Feature Branch**: `003-autenticacion-empleados`  
**Created**: 2026-03-11  
**Status**: Draft  
**Input**: User description: "se hara un una nueva implementacion, el sistema tendra una autenticacion de los empleados con correo y contraseña"

## Clarifications

### Session 2026-03-11

- Q: ¿Cuál es la política mínima de contraseña para autenticación de empleados? → A: Mínimo 8 caracteres, al menos 1 letra y 1 número.
- Q: ¿Cuál es la política de bloqueo tras intentos fallidos consecutivos? → A: Bloqueo temporal de 15 minutos tras 5 intentos fallidos consecutivos.
- Q: ¿Se implementará endpoint dedicado de login en esta feature? → A: No; la autenticación se valida al acceder a endpoints protegidos con HTTP Basic Auth.
- Q: ¿Cómo se comporta el desbloqueo tras el bloqueo temporal? → A: Desbloqueo automático al cumplir 15 minutos y reinicio del contador de fallos.
- Q: ¿Qué alcance de autorización aplica a endpoints CRUD de empleados? → A: Cualquier empleado autenticado puede hacer CRUD completo de empleados.

## User Scenarios & Testing _(mandatory)_

<!--
  IMPORTANT: User stories should be PRIORITIZED as user journeys ordered by importance.
  Each user story/journey must be INDEPENDENTLY TESTABLE - meaning if you implement just ONE of them,
  you should still have a viable MVP (Minimum Viable Product) that delivers value.

  Assign priorities (P1, P2, P3, etc.) to each story, where P1 is the most critical.
  Think of each story as a standalone slice of functionality that can be:
  - Developed independently
  - Tested independently
  - Deployed independently
  - Demonstrated to users independently
-->

### User Story 1 - Iniciar sesión como empleado (Priority: P1)

Como empleado, quiero autenticarme con mi correo y contraseña para acceder a recursos del sistema que requieren identificación.

**Why this priority**: sin autenticación no hay control de acceso y la funcionalidad principal del sistema no puede usarse de forma segura.

**Independent Test**: se valida intentando autenticación con credenciales válidas e inválidas y verificando acceso permitido o denegado según corresponda.

**Acceptance Scenarios**:

1. **Given** que el empleado tiene una cuenta activa con correo y contraseña válidos, **When** envía sus credenciales para autenticarse, **Then** el sistema confirma autenticación exitosa y le permite acceder a recursos protegidos.
2. **Given** que el correo o la contraseña no coinciden con una cuenta activa, **When** intenta autenticarse, **Then** el sistema rechaza el acceso con mensaje de credenciales inválidas.

---

### User Story 2 - Proteger operaciones de empleados (Priority: P2)

Como responsable del sistema, quiero que los endpoints de gestión de empleados exijan autenticación para evitar consultas o cambios no autorizados.

**Why this priority**: asegura confidencialidad e integridad de la información de empleados una vez habilitado el login.

**Independent Test**: se valida accediendo a endpoints protegidos con y sin autenticación, comprobando respuesta autorizada o no autorizada.

**Acceptance Scenarios**:

1. **Given** que un usuario no autenticado invoca un endpoint protegido de empleados, **When** realiza la solicitud, **Then** el sistema responde acceso no autorizado.
2. **Given** que un empleado autenticado invoca un endpoint protegido de empleados, **When** realiza la solicitud, **Then** el sistema procesa la operación según las reglas de negocio.

---

### User Story 3 - Gestionar credenciales de acceso (Priority: P3)

Como empleado autenticado, quiero registrar y mantener el correo de acceso de cada empleado para que cada cuenta sea única y trazable.

**Why this priority**: permite operar la autenticación de forma sostenible, evitando cuentas ambiguas o duplicadas.

**Independent Test**: se valida creando y actualizando datos de acceso de empleados y verificando validaciones de formato, unicidad y obligatoriedad.

**Acceptance Scenarios**:

1. **Given** que se registra o actualiza un empleado con correo válido no usado previamente, **When** se guarda la información, **Then** el sistema acepta el cambio y asocia ese correo como identificador de acceso.
2. **Given** que se intenta registrar o actualizar un empleado con un correo ya asignado a otro empleado, **When** se guarda la información, **Then** el sistema rechaza la operación por duplicidad.

---

### Edge Cases

- ¿Qué ocurre si el correo llega vacío, con formato inválido o con espacios no permitidos? El sistema debe rechazar la autenticación o actualización de credenciales según corresponda.
- ¿Qué ocurre si la contraseña llega vacía o no cumple la política mínima (8 caracteres, al menos 1 letra y 1 número)? El sistema debe rechazar la operación y no habilitar acceso.
- ¿Qué ocurre si un empleado está inactivo o bloqueado? El sistema debe rechazar autenticación aunque las credenciales sean correctas.
- ¿Qué ocurre ante múltiples intentos fallidos consecutivos? El sistema debe bloquear temporalmente la cuenta por 15 minutos tras 5 fallos consecutivos y registrar el evento de seguridad.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST permitir autenticación de empleados usando correo y contraseña.
- **FR-002**: El sistema MUST validar formato de correo y obligatoriedad de correo y contraseña antes de procesar autenticación.
- **FR-011**: El sistema MUST exigir política mínima de contraseña de 8 caracteres con al menos 1 letra y 1 número.
- **FR-003**: El sistema MUST rechazar autenticaciones con credenciales inválidas sin exponer si falló correo o contraseña.
- **FR-004**: El sistema MUST permitir acceso a recursos protegidos solo a empleados autenticados.
- **FR-005**: El sistema MUST exigir unicidad del correo de acceso entre empleados.
- **FR-006**: El sistema MUST permitir registrar y actualizar correo de acceso de empleados con validaciones de formato y duplicidad.
- **FR-007**: El sistema MUST almacenar la contraseña de forma no reversible y nunca devolverla en respuestas funcionales.
- **FR-008**: El sistema MUST registrar eventos de autenticación exitosa y fallida para trazabilidad operativa.
- **FR-009**: El sistema MUST devolver un payload de error uniforme `ApiError` con campos `timestamp`, `status`, `error`, `message`, `path` para errores 400, 401, 409 y 423.
- **FR-010**: El sistema MUST exigir autenticación en todos los endpoints protegidos versionados bajo `/api/v1/empleados/**`. Los endpoints públicos permitidos en este alcance son únicamente documentación técnica (`/v3/api-docs`, `/swagger-ui/**`).
- **FR-012**: El sistema MUST bloquear temporalmente por 15 minutos una cuenta de empleado tras 5 intentos fallidos consecutivos de autenticación.
- **FR-013**: El sistema MUST validar autenticación únicamente en el acceso a endpoints protegidos mediante HTTP Basic Auth, sin exponer endpoint dedicado de login en este alcance.
- **FR-014**: El sistema MUST desbloquear automáticamente la cuenta al cumplirse 15 minutos de bloqueo y reiniciar el contador de intentos fallidos.
- **FR-015**: El sistema MUST permitir que cualquier empleado autenticado ejecute operaciones CRUD completas del módulo de empleados en esta versión.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La funcionalidad MUST operar bajo Java 17 y Spring Boot 3, en cumplimiento de la constitución vigente.
- **CA-002**: La autenticación de recursos protegidos MUST usar HTTP Basic Auth, empleando el correo del empleado como identificador de acceso.
- **CA-003**: Los datos de autenticación y empleados MUST persistirse en PostgreSQL y el aprovisionamiento local/desarrollo MUST estar disponible mediante Docker/Docker Compose.
- **CA-004**: La especificación de endpoints de autenticación y recursos protegidos MUST reflejarse en el contrato OpenAPI publicado y visible en Swagger para desarrollo.
- **CA-005**: Los endpoints públicos relacionados MUST mantener versionado explícito en ruta con prefijo `/api/v1/...`.
- **CA-006**: Las consultas de colección del dominio empleados MUST conservar paginación determinista con tamaño fijo de 10 instancias por solicitud.

### Assumptions

- El sistema autentica a empleados previamente registrados; no se incluye auto-registro público en este alcance.
- El correo representa el identificador único de acceso por empleado.
- El bloqueo por intentos fallidos se levanta automáticamente tras 15 minutos y reinicia contador.
- La funcionalidad debe coexistir con el módulo CRUD de empleados ya definido en especificaciones anteriores.
- Esta feature no incluye endpoint específico de login; la validación de credenciales ocurre en cada solicitud a recursos protegidos.
- Esta versión no distingue autorizaciones por rol dentro del CRUD; todo empleado autenticado puede operar alta, consulta, modificación y eliminación.

### Key Entities _(include if feature involves data)_

- **Cuenta de acceso de empleado**: representa las credenciales de autenticación de un empleado, incluyendo correo único, contraseña almacenada de forma segura y estado de acceso (activo/bloqueado).
- **Evento de autenticación**: representa cada intento de acceso con resultado (éxito o fallo), marca temporal e identidad de empleado/correo para trazabilidad.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: El 100% de empleados con credenciales válidas puede autenticarse y acceder a recursos protegidos en su primer intento.
- **SC-002**: El 100% de intentos con credenciales inválidas es rechazado sin otorgar acceso.
- **SC-003**: Al menos el 95% de autenticaciones válidas completa el proceso en menos de 2 segundos en entorno local de validación, medido sobre 10 ejecuciones consecutivas con umbral p95.
- **SC-004**: El 100% de endpoints protegidos de empleados rechaza solicitudes no autenticadas con respuesta consistente de no autorizado.
- **SC-005**: El 100% de correos de acceso almacenados para empleados se mantiene único, sin duplicidades activas.
- **SC-006**: El 100% de autenticaciones con contraseñas que no cumplen la política mínima es rechazado.
- **SC-007**: El 100% de cuentas que alcancen 5 intentos fallidos consecutivos queda bloqueado durante 15 minutos.
- **SC-008**: El 100% de cuentas bloqueadas recupera acceso automáticamente al cumplirse 15 minutos, con contador de fallos reiniciado.
- **SC-009**: El 100% de solicitudes CRUD realizadas por empleados autenticados válidos se procesa sin rechazo por rol en esta versión.
