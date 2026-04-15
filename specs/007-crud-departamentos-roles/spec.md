# Feature Specification: CRUD visual de departamentos con permisos por rol

**Feature Branch**: `007-crud-departamentos-roles`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "crea la parte visual del crud de departamentos tras loguearse, el usuario admin es el que tendra permisos completo con las operaciones crud mientras que el empleado podra ver sin opciones de modificar o eliminar"

## Clarifications

### Session 2026-03-26

- Q: ¿Cómo deben presentarse en UI las acciones restringidas para `empleado`? → A: Mostrar acciones restringidas deshabilitadas.
- Q: ¿Qué hacer cuando un `empleado` accede directamente a rutas restringidas? → A: Redirigir al listado en modo lectura y mostrar mensaje de permisos insuficientes.
- Q: ¿Qué mensaje usar para intentos de acción restringida? → A: Mensaje único global `No tienes permisos para esta acción.`
- Q: ¿Cómo manejar acceso sin sesión o con sesión expirada? → A: Redirigir a login con mensaje `Sesión expirada o no autenticada.`.
- Q: ¿Qué ocurre si la sesión expira durante edición admin y al enviar cambios? → A: Se cancela el guardado, se redirige a login y se muestra `Sesión expirada o no autenticada.` sin persistir cambios.
- Q: ¿Cómo debe verse el listado vacío de departamentos? → A: Mostrar `No hay departamentos para mostrar.`, sin filas de datos y con controles de paginación deshabilitados.
- Q: ¿Cómo se debe medir SC-003 para identificar permisos por rol? → A: Prueba moderada de usabilidad con 20 usuarios representativos, cronómetro y registro de aciertos/tiempos.

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Gestión completa para admin (Priority: P1)

Como usuario admin autenticado, quiero administrar departamentos desde la interfaz para crear, editar y eliminar registros según necesidad operativa.

**Why this priority**: Es el valor principal de la funcionalidad solicitada y habilita la administración completa del catálogo de departamentos.

**Independent Test**: Puede validarse iniciando sesión con un usuario admin y completando en una sola sesión los flujos de alta, edición y eliminación desde el módulo de departamentos.

**Acceptance Scenarios**:

1. **Given** que un admin inició sesión correctamente, **When** accede al módulo de departamentos, **Then** visualiza acciones habilitadas para crear, editar y eliminar.
2. **Given** que un admin está en el módulo de departamentos, **When** crea un departamento con datos válidos, **Then** el nuevo registro aparece en el listado actualizado.
3. **Given** que un admin selecciona un departamento existente, **When** modifica su información y confirma, **Then** los cambios se reflejan en el listado.
4. **Given** que un admin selecciona un departamento permitido para eliminación, **When** confirma la eliminación, **Then** el departamento deja de mostrarse en el listado.

---

### User Story 2 - Consulta en modo lectura para empleado (Priority: P2)

Como usuario empleado autenticado, quiero ver los departamentos disponibles sin opciones de modificar o eliminar para consultar información sin riesgo de cambios no autorizados.

**Why this priority**: Garantiza control de acceso por rol y protege la integridad de los datos de departamentos.

**Independent Test**: Puede validarse iniciando sesión con usuario empleado y comprobando que solo se permite la visualización del módulo de departamentos.

**Acceptance Scenarios**:

1. **Given** que un empleado inició sesión, **When** entra al módulo de departamentos, **Then** puede ver el listado de departamentos.
2. **Given** que un empleado está en el listado de departamentos, **When** inspecciona la interfaz, **Then** visualiza acciones de crear, editar y eliminar en estado deshabilitado.
3. **Given** que un empleado intenta disparar una acción de modificación desde la UI, **When** se procesa el intento, **Then** la operación es bloqueada y se informa falta de permisos.

---

### User Story 3 - Consistencia de permisos entre sesiones (Priority: P3)

Como usuario autenticado, quiero que la interfaz adapte los permisos visibles según el rol activo en cada sesión para evitar comportamientos inconsistentes al cambiar de usuario.

**Why this priority**: Mejora claridad de uso y reduce errores por estados residuales entre sesiones con roles distintos.

**Independent Test**: Puede validarse iniciando sesión como admin, cerrando sesión y entrando como empleado, verificando que la UI actualiza capacidades según el nuevo rol.

**Acceptance Scenarios**:

1. **Given** que un usuario inicia sesión como admin y luego como empleado, **When** vuelve al módulo de departamentos, **Then** la UI refleja únicamente permisos de lectura.
2. **Given** que un usuario inicia sesión como empleado y luego como admin, **When** accede nuevamente al módulo, **Then** la UI muestra acciones completas de CRUD.

---

### Edge Cases

- Si un usuario no autenticado (o con sesión expirada) intenta abrir la ruta del módulo de departamentos, se redirige a login y se muestra `Sesión expirada o no autenticada.`.
- Si la sesión expira mientras un admin edita y confirma cambios, la operación se cancela, no se persisten cambios y se redirige a login con `Sesión expirada o no autenticada.`.
- Si el listado de departamentos está vacío, la interfaz muestra `No hay departamentos para mostrar.`, no renderiza filas y deshabilita navegación de paginación.
- Si un empleado intenta acceder por URL a una vista de creación o edición, se redirige al listado en modo lectura y se muestra mensaje de permisos insuficientes.
- Cuando una operación se rechaza por permisos insuficientes (403), la interfaz muestra `No tienes permisos para esta acción.`.
- Cuando la operación falla por sesión inválida o expirada (401), la interfaz redirige a login y muestra `Sesión expirada o no autenticada.`.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST proveer un módulo visual de departamentos accesible únicamente después de autenticación exitosa.
- **FR-002**: El sistema MUST permitir al usuario con rol admin ejecutar operaciones de crear, editar y eliminar departamentos desde la interfaz.
- **FR-003**: El sistema MUST permitir al usuario con rol empleado visualizar el listado y detalle de departamentos en modo lectura.
- **FR-004**: El sistema MUST mostrar en la interfaz las acciones de crear, editar y eliminar en estado deshabilitado cuando el usuario no tenga permisos de admin.
- **FR-005**: El sistema MUST bloquear cualquier intento de modificación iniciado por usuarios sin permisos y, ante navegación directa a pantallas de edición/alta, redirigir al listado en modo lectura.
- **FR-006**: El sistema MUST mostrar el mensaje `No tienes permisos para esta acción.` cuando se rechace una acción restringida.
- **FR-007**: El sistema MUST reflejar en la vista el estado actualizado del listado de departamentos tras operaciones exitosas de alta, edición o eliminación realizadas por admin.
- **FR-008**: El sistema MUST reevaluar los permisos de interfaz en cada inicio de sesión y al cambiar de sesión entre usuarios con roles distintos.
- **FR-009**: El sistema MUST, tras recargar la página durante una sesión activa, conservar el rol efectivo y mostrar de forma consistente el mismo estado de permisos: `ADMIN` con acciones CRUD habilitadas y `EMPLEADO` con acciones CRUD visibles deshabilitadas.
- **FR-010**: El sistema MUST redirigir al login con el mensaje `Sesión expirada o no autenticada.` cuando se intente acceder al módulo sin sesión válida.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La feature MUST mantenerse sobre Spring Boot 3 + Java 17 para backend y Angular 21 LTS para frontend, conforme a la constitución del proyecto.
- **CA-002**: El acceso al módulo de departamentos MUST respetar autenticación HTTP Basic y autorización por rol para recursos protegidos.
- **CA-003**: La persistencia de departamentos MUST continuar en PostgreSQL y el entorno local/dev MUST seguir aprovisionable con Docker Compose.
- **CA-004**: Los contratos de API asociados al módulo de departamentos MUST mantenerse documentados y publicados en OpenAPI/Swagger.
- **CA-005**: Las rutas públicas consumidas por frontend MUST conservar versionado explícito con prefijo `/api/v1/...`.
- **CA-006**: Las consultas de colección de departamentos MUST respetar paginación con máximo de 10 elementos por solicitud.

### Assumptions

- El rol del usuario autenticado está disponible para el frontend durante la sesión para resolver permisos de interfaz.
- Las validaciones de negocio de departamentos (campos obligatorios y reglas de integridad) ya existen en backend y esta feature no las redefine.
- El alcance se limita al comportamiento visual y de permisos del CRUD de departamentos tras login, sin cambiar el modelo de roles existente.

### Key Entities _(include if feature involves data)_

- **Usuario autenticado**: Identidad en sesión con rol operativo (`admin` o `empleado`) que condiciona acciones disponibles en la UI.
- **Departamento**: Registro de negocio que puede ser consultado por ambos roles y gestionado completamente solo por admin.
- **Permiso de acción en interfaz**: Regla que define visibilidad/habilitación de acciones CRUD según rol activo.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: En pruebas funcionales con rol admin, 100% de los casos de crear, editar y eliminar departamentos se completan exitosamente cuando los datos son válidos.
- **SC-002**: En pruebas funcionales con rol empleado, 0 operaciones de crear, editar o eliminar departamentos resultan exitosas.
- **SC-003**: En una prueba moderada de usabilidad con 20 usuarios representativos (10 `ADMIN`, 10 `EMPLEADO`), al menos 95% identifica correctamente sus permisos en menos de 10 segundos desde la carga del módulo, registrando acierto y tiempo por ejecución con cronómetro.
- **SC-004**: El 100% de intentos de acceso directo a acciones restringidas por parte de rol empleado termina en bloqueo de acción y feedback de permisos insuficientes.
