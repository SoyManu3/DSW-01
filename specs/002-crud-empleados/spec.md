# Feature Specification: CRUD de empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "CRUD de empleados con `clave` con prefijo `EMP-` seguido de autonumérico como PK compuesta, y `nombre`, `direccion`, `telefono` de hasta 100 caracteres"

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Registrar empleado (Priority: P1)

Como usuario administrativo, quiero registrar un empleado con nombre, dirección y teléfono para que el sistema genere automáticamente una clave con formato `EMP-` + número correlativo y mantener un padrón confiable.

**Why this priority**: sin alta de empleados no existe base de datos funcional para operar el resto del módulo.

**Independent Test**: se valida creando un empleado nuevo con datos válidos, verificando que el sistema genera una clave con formato correcto y que el registro queda disponible para consulta posterior.

**Acceptance Scenarios**:

1. **Given** que no existen empleados previos, **When** registro un empleado con nombre, dirección y teléfono válidos, **Then** el sistema confirma creación y genera clave `EMP-1`.
2. **Given** que ya existe el último correlativo `EMP-27`, **When** registro un nuevo empleado válido, **Then** el sistema genera `EMP-28` y persiste el empleado.
3. **Given** que se ingresa un nombre de más de 100 caracteres, **When** intento crear el empleado, **Then** el sistema rechaza la solicitud con validación de longitud.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario administrativo, quiero consultar empleados por clave y listar empleados con paginación fija, para ubicar información de forma rápida sin respuestas masivas.

**Why this priority**: después de registrar datos, la consulta es la necesidad principal para uso diario y verificación operativa.

**Independent Test**: se valida listando empleados existentes por páginas de 10 elementos y recuperando uno puntual por su clave.

**Acceptance Scenarios**:

1. **Given** que existen más de 10 empleados registrados, **When** consulto `GET /api/v1/empleados?page=0`, **Then** obtengo como máximo 10 empleados y metadatos de paginación.
2. **Given** que existen más de 10 empleados registrados, **When** consulto `GET /api/v1/empleados?page=1`, **Then** obtengo la siguiente página con hasta 10 empleados.
3. **Given** que existe el empleado con clave `EMP-1`, **When** consulto `GET /api/v1/empleados/EMP-1`, **Then** el sistema devuelve el registro correcto.
4. **Given** que no existe la clave `EMP-9999`, **When** consulto `GET /api/v1/empleados/EMP-9999`, **Then** el sistema responde que el empleado no fue encontrado.

---

### User Story 3 - Actualizar y eliminar empleado (Priority: P3)

Como usuario administrativo, quiero actualizar y eliminar empleados para mantener la información vigente y depurada.

**Why this priority**: aunque es esencial para mantenimiento, depende de que exista previamente información cargada y consultable.

**Independent Test**: se valida actualizando campos de un empleado existente y luego eliminando otro, verificando ambos resultados.

**Acceptance Scenarios**:

1. **Given** que existe `EMP-1`, **When** actualizo nombre, dirección o teléfono con valores válidos, **Then** el sistema guarda los cambios.
2. **Given** que existe `EMP-1`, **When** envío una actualización con teléfono mayor a 100 caracteres, **Then** el sistema rechaza la actualización por validación.
3. **Given** que existe `EMP-2`, **When** solicito su eliminación, **Then** el sistema elimina el registro y deja de retornarlo en consultas.

### Edge Cases

- ¿Qué ocurre si algún campo obligatorio llega vacío o solo con espacios? El sistema debe rechazar la solicitud por datos incompletos.
- ¿Qué ocurre si se intenta enviar manualmente una clave en la creación? El sistema debe ignorarla o rechazarla según contrato y siempre autogenerar `EMP-<n>`.
- ¿Qué ocurre si la clave cambia en una actualización? El sistema debe impedir cambio de clave porque identifica de forma única al empleado.
- ¿Qué ocurre si se solicita eliminar una clave inexistente? El sistema debe responder no encontrado sin alterar otros registros.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear empleados con los campos `nombre`, `direccion` y `telefono`.
- **FR-002**: El sistema MUST generar automáticamente `clave` con formato `EMP-<numero_autonumerico>` al crear empleados.
- **FR-003**: El sistema MUST tratar `clave` como representación externa de una PK compuesta por `prefijo` (valor fijo `EMP`) y `numero_autonumerico`.
- **FR-004**: El sistema MUST permitir consultar un empleado por su `clave`.
- **FR-005**: El sistema MUST permitir listar empleados existentes mediante `GET /api/v1/empleados`.
- **FR-006**: El sistema MUST permitir actualizar `nombre`, `direccion` y `telefono` de un empleado existente.
- **FR-007**: El sistema MUST permitir eliminar empleados por `clave`.
- **FR-008**: El sistema MUST validar que `nombre`, `direccion` y `telefono` tengan longitud máxima de 100 caracteres.
- **FR-009**: El sistema MUST impedir modificaciones manuales de la `clave` en operaciones de actualización.
- **FR-010**: El sistema MUST garantizar unicidad de la `clave` generada mediante el componente autonumérico de la PK compuesta.
- **FR-011**: El sistema MUST rechazar operaciones sobre empleados inexistentes con respuesta de no encontrado.
- **FR-012**: El sistema MUST exponer mensajes de error claros para validaciones y conflictos de datos.
- **FR-013**: El sistema MUST exponer los endpoints públicos bajo rutas versionadas con prefijo `/api/v1`.
- **FR-014**: El sistema MUST paginar consultas de colección devolviendo exactamente 10 instancias por consulta, excepto en la última página donde puede devolver menos.
- **FR-015**: El endpoint de listado MUST aceptar parámetro `page` (base 0) y devolver metadatos de paginación (`page`, `size`, `totalElements`, `totalPages`).

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: La funcionalidad MUST implementarse respetando el runtime y framework definidos por la constitución vigente del proyecto.
- **CA-002**: La funcionalidad MUST publicar reglas de acceso a recursos de empleados conforme al esquema de autenticación definido por la constitución.
- **CA-003**: La funcionalidad MUST persistir datos de empleados en el motor de base de datos definido por la constitución y contemplar provisión de entorno local indicada en dicha constitución.
- **CA-004**: La funcionalidad MUST mantener actualizada la documentación del contrato API conforme al estándar definido por la constitución.
- **CA-005**: La funcionalidad MUST definir y aplicar versionado explícito en rutas de endpoints públicos (`/api/v1/...`).
- **CA-006**: La funcionalidad MUST definir y aplicar paginación de consultas de colección con tamaño fijo de 10 instancias por respuesta.

### Assumptions

- La `clave` no es provista por el usuario en alta; el sistema la autogenera como `EMP-<n>`.
- Internamente, la PK compuesta está formada por `prefijo` fijo (`EMP`) y `numero_autonumerico` incremental.
- En interfaces de consulta y respuesta, la clave se expone como campo único concatenado (`EMP-<n>`).
- Los campos `nombre`, `direccion` y `telefono` son obligatorios para creación.
- Las operaciones CRUD son consumidas por usuarios internos administrativos.
- Las rutas públicas del módulo se exponen bajo `/api/v1/empleados`.
- El listado usa paginación por página (`page`) con tamaño fijo de 10 resultados.

### Key Entities _(include if feature involves data)_

- **Empleado**: representa a una persona registrada con PK compuesta (`prefijo`, `numero_autonumerico`) y clave expuesta (`clave = EMP-<n>`), además de `nombre`, `direccion` y `telefono`.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: El 100% de intentos de alta con datos válidos crea un empleado utilizable en consulta posterior.
- **SC-002**: El 100% de intentos con `nombre`, `direccion` o `telefono` de más de 100 caracteres es rechazado con mensaje de validación.
- **SC-003**: Al menos el 95% de consultas por clave existente retorna el empleado correcto en menos de 2 segundos en entorno de validación.
- **SC-004**: El 100% de operaciones de actualización y eliminación sobre claves inexistentes devuelve respuesta de no encontrado.
- **SC-005**: El 100% de claves generadas en alta cumplen el patrón `EMP-<numero_entero_positivo>` sin duplicados.
- **SC-006**: El 100% de respuestas de `GET /api/v1/empleados` devuelve como máximo 10 instancias y metadatos de paginación válidos.
