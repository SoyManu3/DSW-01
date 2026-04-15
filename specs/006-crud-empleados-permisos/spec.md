# Feature Specification: CRUD visual de empleados con permisos por rol

**Feature Branch**: `006-crud-empleados-permisos`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "crea la parte visual del crud de empleados. Si el usuario admin hace login en el front este tendra la posibilidad de hacer uso completo de las operaciones crud al ingresar y en caso de que un empleado haga login podra solo ver la informacion pero no va poder modificar la informacion o eliminar"

## Clarifications

### Session 2026-03-26

- Q: ¿Cómo determina el frontend si el usuario tiene permisos de admin? → A: El frontend obtiene el rol desde backend y decide permisos con ese valor.
- Q: ¿Qué pasa si un empleado entra por URL a crear/editar? → A: Se redirige al listado en modo lectura con mensaje de permisos insuficientes.

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Administración completa por usuario admin (Priority: P1)

Como usuario con perfil administrador, quiero entrar al módulo visual de empleados y ejecutar crear, editar y eliminar para mantener la información actualizada.

**Why this priority**: Es el objetivo principal de negocio del CRUD y habilita la gestión operativa completa.

**Independent Test**: Puede validarse iniciando sesión con usuario admin y ejecutando el flujo completo de alta, edición y eliminación desde la UI, verificando que todas las acciones estén habilitadas y efectivas.

**Acceptance Scenarios**:

1. **Given** que un admin inició sesión correctamente, **When** abre la pantalla de empleados, **Then** ve habilitadas las acciones de crear, editar y eliminar.
2. **Given** que un admin está en la pantalla de empleados, **When** crea un nuevo empleado con datos válidos, **Then** el registro aparece en el listado actualizado.
3. **Given** que un admin está en la pantalla de empleados, **When** modifica datos de un empleado existente, **Then** el cambio se refleja en la lista sin perder consistencia de datos.
4. **Given** que un admin está en la pantalla de empleados, **When** elimina un empleado permitido por reglas de negocio, **Then** el registro deja de mostrarse en el listado.

---

### User Story 2 - Consulta segura por usuario empleado (Priority: P2)

Como usuario empleado (no administrador), quiero ver la información de empleados sin capacidad de modificarla para consultar datos sin riesgo de cambios no autorizados.

**Why this priority**: Protege la integridad de los datos y cumple la separación de permisos solicitada.

**Independent Test**: Puede validarse iniciando sesión como empleado, accediendo al listado y comprobando que no existen acciones operativas de creación, edición o eliminación disponibles en la interfaz.

**Acceptance Scenarios**:

1. **Given** que un empleado inició sesión, **When** accede al módulo de empleados, **Then** solo puede visualizar la información.
2. **Given** que un empleado inició sesión, **When** navega por la pantalla de empleados, **Then** no encuentra controles habilitados para crear, editar ni eliminar.
3. **Given** que un empleado intenta forzar una acción de modificación desde la UI, **When** la acción se dispara, **Then** el sistema bloquea la operación y muestra un mensaje de permisos insuficientes.

---

### User Story 3 - Experiencia clara según rol al iniciar sesión (Priority: P3)

Como usuario autenticado, quiero que la interfaz muestre claramente el alcance de mis permisos al entrar para entender de inmediato qué acciones puedo realizar.

**Why this priority**: Reduce errores de uso y mejora la comprensión funcional del sistema según rol.

**Independent Test**: Puede validarse iniciando sesión con ambos perfiles y comparando el estado visible de acciones y mensajes de permisos en la misma pantalla.

**Acceptance Scenarios**:

1. **Given** dos usuarios con roles distintos (admin y empleado), **When** cada uno ingresa al módulo de empleados, **Then** la interfaz refleja consistentemente las capacidades de su rol.
2. **Given** que un usuario cambia de sesión entre roles, **When** vuelve a la pantalla de empleados, **Then** la interfaz actualiza el estado de permisos sin conservar acciones del rol anterior.

### Edge Cases

- ¿Qué pasa si un usuario no autenticado intenta abrir directamente la ruta del CRUD visual de empleados?
- ¿Qué ocurre si la sesión expira mientras un admin está editando un registro?
- ¿Cómo se comporta la UI si la consulta de empleados devuelve una lista vacía?
- Si un empleado intenta acceder mediante URL a una vista de creación/edición, la UI redirige al listado en modo lectura y muestra un mensaje de permisos insuficientes.
- ¿Qué pasa si el backend responde error de autorización al eliminar o actualizar por un desfase de estado de sesión?

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST proveer una pantalla visual de gestión de empleados accesible para usuarios autenticados, compuesta por listado, formulario de creación/edición y vista de detalle en modo lectura.
- **FR-002**: El sistema MUST habilitar operaciones de crear, editar y eliminar empleados únicamente para usuarios autenticados con rol administrador.
- **FR-003**: El sistema MUST permitir que usuarios autenticados con rol empleado solo puedan visualizar el listado y detalle de empleados, sin capacidad de modificación.
- **FR-004**: La interfaz MUST ocultar o deshabilitar de forma explícita los controles de creación, edición y eliminación cuando el usuario autenticado no tenga permisos de administrador.
- **FR-005**: El sistema MUST mostrar el mensaje `No tienes permisos para esta acción.` cuando una operación de creación, edición o eliminación sea rechazada por permisos (incluyendo respuestas 401/403).
- **FR-006**: El sistema MUST refrescar la vista de empleados tras operaciones exitosas de alta, edición o eliminación para reflejar el estado actual de datos.
- **FR-007**: El sistema MUST mantener comportamiento consistente de permisos después de cierre de sesión e inicio de sesión con otro rol.
- **FR-008**: El sistema MUST impedir acceso funcional a acciones de modificación para usuarios no autorizados incluso si intentan invocarlas manualmente desde la interfaz.
- **FR-009**: El sistema MUST permitir al rol empleado navegar el listado paginado y abrir el detalle de un empleado en modo lectura, sin exponer controles de modificación.
- **FR-010**: El frontend MUST resolver permisos de UI a partir del rol entregado por backend en la sesión autenticada y no por comparación de identificadores de usuario específicos.
- **FR-011**: Si un usuario sin permisos de edición accede por URL a rutas de creación o edición, el sistema MUST redirigir al listado de empleados en modo lectura y mostrar mensaje de permisos insuficientes.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La funcionalidad MUST mantenerse sobre Spring Boot 3 + Java 17 para backend y Angular 21 LTS para frontend, de acuerdo con la constitución vigente.
- **CA-002**: La autenticación y protección de recursos MUST continuar usando HTTP Basic Auth y aplicar control de acceso por rol en operaciones del CRUD de empleados.
- **CA-003**: La información de empleados MUST mantenerse en PostgreSQL y el entorno local/dev MUST seguir siendo aprovisionable con Docker Compose sin cambiar esta estrategia.
- **CA-004**: La funcionalidad MUST mantener contrato API documentado en OpenAPI y publicación en Swagger para los endpoints usados por el CRUD visual.
- **CA-005**: Las rutas públicas consumidas por la UI MUST conservar versionado explícito con prefijo `/api/v1/...`.
- **CA-006**: La consulta de colecciones de empleados MUST conservar paginación determinista de 10 instancias por solicitud.

### Assumptions

- El backend provee explícitamente el rol del usuario autenticado de forma consumible por el frontend durante la sesión.
- El rol empleado representa a usuarios autenticados sin privilegios de modificación.
- Las reglas de negocio para validaciones de datos de empleado ya están definidas por el backend y solo deben respetarse desde la UI.
- El alcance de esta feature se limita al CRUD visual de empleados y control de permisos en frontend, sin alterar el modelo de datos de empleados.

### Key Entities _(include if feature involves data)_

- **Usuario autenticado**: Representa la identidad en sesión y su rol operativo (admin o empleado) que define permisos visibles y ejecutables.
- **Empleado**: Registro de negocio consultado y, solo para admin, gestionado mediante operaciones CRUD.
- **Permiso de acción UI**: Regla funcional que determina si una acción del CRUD se muestra/habilita o se bloquea según rol.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: 100% de los usuarios admin autenticados pueden completar de extremo a extremo crear, editar y eliminar empleados en una sesión de prueba.
- **SC-002**: 100% de los usuarios empleados autenticados pueden visualizar el listado de empleados sin disponer de acciones de modificación.
- **SC-003**: En pruebas funcionales por rol, 0 operaciones de creación, edición o eliminación iniciadas por usuario empleado resultan exitosas.
- **SC-004**: En una prueba guiada de 20 ejecuciones (10 con rol `ADMIN` y 10 con rol `EMPLEADO`), al menos 19/20 usuarios identifican correctamente sus capacidades en menos de 10 segundos tras ingresar al módulo.
