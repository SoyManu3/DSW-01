# Feature Specification: CRUD de Departamentos y Relación con Empleados

**Feature Branch**: `004-crud-departamentos`  
**Created**: 2026-03-12  
**Status**: Draft  
**Input**: User description: "se creara una nueva tabla, la tabla departamentos con los atributos: id y nombre. Igualmente debe tener un crud. esta tabla debe tener una relacion con la entidad empleados una relacion de uno a muchos"

## Clarifications

### Session 2026-03-12

- Q: ¿Cómo se evalúa la unicidad del nombre de departamento? → A: Unicidad sensible a mayúsculas/minúsculas (`Ventas` y `ventas` se permiten).
- Q: ¿Cómo se exponen los empleados asociados al consultar un departamento? → A: `GET` de detalle devuelve datos del departamento y contador de empleados; el listado de empleados se consulta en un endpoint paginado específico por departamento.
- Q: ¿Puede existir un empleado sin departamento asociado? → A: No, todo empleado debe pertenecer obligatoriamente a un departamento.
- Q: ¿Cuál es el orden por defecto en listados paginados? → A: Orden obligatorio por `id` ascendente.
- Q: ¿Cuál es la longitud máxima del nombre de departamento? → A: Máximo 100 caracteres.

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Gestionar departamentos (Priority: P1)

Como usuario autenticado del sistema, quiero crear, consultar, actualizar y eliminar departamentos para mantener organizada la estructura interna de la empresa.

**Why this priority**: Sin CRUD de departamentos no existe la entidad solicitada ni se puede administrar el catálogo base para relacionarlo con empleados.

**Independent Test**: Puede probarse de forma independiente ejecutando operaciones de alta, consulta, modificación y baja de departamentos y validando respuestas correctas y persistencia.

**Acceptance Scenarios**:

1. **Given** que el usuario está autenticado, **When** registra un departamento con nombre válido, **Then** el sistema crea el departamento y devuelve su identificador y nombre.
2. **Given** que existen departamentos registrados, **When** el usuario consulta la colección de departamentos, **Then** recibe resultados paginados con un máximo de 10 elementos por respuesta.
3. **Given** que existe un departamento, **When** el usuario actualiza su nombre con un valor válido, **Then** el sistema guarda el nuevo nombre y lo devuelve en la respuesta.
4. **Given** que existe un departamento sin empleados asociados, **When** el usuario lo elimina, **Then** el sistema confirma la eliminación y el recurso deja de estar disponible.

---

### User Story 2 - Asignar empleados a un departamento (Priority: P2)

Como usuario autenticado, quiero vincular empleados con un departamento para reflejar la relación uno a muchos (un departamento, muchos empleados).

**Why this priority**: La relación solicitada entre entidades aporta valor funcional al modelo y permite organizar empleados por área.

**Independent Test**: Puede probarse creando un departamento, asociando múltiples empleados al mismo y verificando que todos queden vinculados correctamente.

**Acceptance Scenarios**:

1. **Given** que existe un departamento y un empleado, **When** el empleado se asocia al departamento, **Then** la relación queda registrada y visible en consultas posteriores.
2. **Given** que un departamento tiene varios empleados asociados, **When** se consulta el detalle del departamento, **Then** el sistema permite identificar los empleados pertenecientes a ese departamento.
3. **Given** que un departamento tiene empleados asociados, **When** se consulta la colección de empleados del departamento, **Then** el sistema devuelve resultados paginados con un máximo de 10 elementos por respuesta.

---

### User Story 3 - Proteger consistencia de datos en operaciones de borrado (Priority: P3)

Como usuario autenticado, quiero reglas claras al eliminar departamentos para evitar datos inconsistentes cuando existen empleados asociados.

**Why this priority**: Evita pérdida de trazabilidad y errores operativos al manejar relaciones entre entidades.

**Independent Test**: Puede probarse intentando eliminar un departamento con empleados asociados y verificando que el sistema aplica la regla de negocio definida.

**Acceptance Scenarios**:

1. **Given** que un departamento tiene empleados asociados, **When** el usuario intenta eliminarlo, **Then** el sistema rechaza la operación con un mensaje de negocio claro.

### Edge Cases

- Intento de crear o actualizar un departamento con nombre vacío, nulo o solo espacios.
- Intento de crear o actualizar un departamento con nombre mayor a 100 caracteres.
- Intento de crear un departamento con un nombre ya existente.
- Consulta o modificación de un departamento inexistente.
- Eliminación de un departamento con empleados asociados.
- Intento de crear o actualizar un empleado sin departamento asociado.
- Solicitud de página fuera de rango en el listado de departamentos.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear departamentos con los atributos `id` y `nombre`, asignando el identificador de forma única.
- **FR-002**: El sistema MUST permitir obtener un departamento por su identificador.
- **FR-003**: El sistema MUST permitir listar departamentos en formato paginado con un máximo de 10 instancias por consulta y orden por defecto `id` ascendente.
- **FR-004**: El sistema MUST permitir actualizar el nombre de un departamento existente.
- **FR-005**: El sistema MUST permitir eliminar un departamento solo cuando no tenga empleados asociados.
- **FR-006**: El sistema MUST rechazar la creación o actualización de departamentos con nombre inválido (vacío, solo espacios o mayor a 100 caracteres).
- **FR-007**: El sistema MUST rechazar la creación de departamentos con nombre duplicado bajo comparación sensible a mayúsculas/minúsculas.
- **FR-008**: El sistema MUST registrar una relación de uno a muchos entre departamentos y empleados, donde un departamento puede tener múltiples empleados y cada empleado pertenece obligatoriamente a un único departamento.
- **FR-009**: El sistema MUST exponer el detalle de departamento con los datos del departamento y un contador de empleados asociados.
- **FR-010**: El sistema MUST exponer una consulta paginada de empleados por departamento con un máximo de 10 instancias por respuesta y orden por defecto `id` ascendente.
- **FR-011**: El sistema MUST devolver respuestas de error consistentes cuando un departamento no exista o una operación viole reglas de negocio.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La funcionalidad MUST ejecutarse y mantenerse sobre Spring Boot 3 y Java 17.
- **CA-002**: Los endpoints de departamentos y su relación con empleados MUST requerir HTTP Basic Auth, salvo endpoints declarados explícitamente como públicos (si existieran).
- **CA-003**: La persistencia MUST usar PostgreSQL y el aprovisionamiento local/dev MUST ser reproducible con Docker Compose.
- **CA-004**: La funcionalidad MUST actualizar el contrato OpenAPI y mantener documentación Swagger accesible en entornos de desarrollo.
- **CA-005**: Todas las rutas públicas de la funcionalidad MUST incluir versión explícita en la URL base (formato `/api/v1/...`).
- **CA-006**: Las consultas de colecciones de departamentos MUST ser paginadas con tamaño fijo de 10 elementos por respuesta.

### Key Entities _(include if feature involves data)_

- **Departamento**: Unidad organizativa con `id` y `nombre`; agrupa múltiples empleados.
- **Empleado**: Persona registrada en el sistema que pertenece a un único departamento dentro de esta funcionalidad.

### Assumptions

- Solo usuarios autenticados pueden operar el CRUD de departamentos.
- El nombre de departamento debe ser único bajo comparación sensible a mayúsculas/minúsculas para evitar ambigüedad operativa.
- El nombre de departamento tiene una longitud máxima de 100 caracteres.
- Todo empleado debe tener un departamento asociado; no se permite `departamento_id` nulo.
- La relación de empleados con departamentos se gestiona dentro del mismo dominio funcional existente de empleados.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: El 100% de operaciones CRUD de departamentos definidas en esta especificación puede ejecutarse exitosamente mediante pruebas de aceptación.
- **SC-002**: El 100% de listados de departamentos respeta el límite máximo de 10 elementos por respuesta.
- **SC-003**: Al menos 95% de intentos de eliminación de departamentos con empleados asociados son rechazados con mensaje de negocio claro en pruebas repetidas.
- **SC-004**: Al menos 90% de usuarios funcionales de prueba completa la creación y asignación de un departamento a empleados sin asistencia adicional en su primer intento.
