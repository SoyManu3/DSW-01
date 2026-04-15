# Feature Specification: Pantallas de Login de Empleados

**Feature Branch**: `005-pantallas-login-empleados`  
**Created**: 2026-03-25  
**Status**: Draft  
**Input**: User description: "crea el apartados visual (pantallas) para el login de empleados, para las pruebas de frontend usaras cypress y estara en angular en la version establecida de la constitucion"

## Clarifications

### Session 2026-03-25

- Q: ¿Cuál es el identificador de login del empleado en la UI? → A: Email corporativo + contraseña, alineado con API.
- Q: ¿Cuál es el destino post-login para esta feature? → A: Redirección única a `/inicio`.
- Q: ¿Cómo debe ser el mensaje de error de autenticación? → A: Mensaje genérico único `Credenciales inválidas`.
- Q: ¿Cómo se ejecutarán las pruebas E2E de frontend para esta feature? → A: Cypress contra backend real local.
- Q: ¿Qué nivel de accesibilidad se requiere para login? → A: Accesibilidad base WCAG 2.1 AA (teclado, labels y foco visible).

## User Scenarios & Testing _(mandatory)_

### User Story 1 - Acceso visual al sistema (Priority: P1)

Como empleado, quiero una pantalla de inicio de sesión clara y usable para autenticarme y entrar al sistema sin ambigüedad.

**Why this priority**: Sin esta pantalla no existe punto de entrada funcional para usuarios autenticados.

**Independent Test**: Puede validarse de forma independiente comprobando que un empleado puede completar el formulario, enviarlo y recibir retroalimentación visual de éxito o error.

**Acceptance Scenarios**:

1. **Given** que el empleado abre la aplicación, **When** visualiza la pantalla de login, **Then** ve campos para email corporativo y contraseña, acción principal para iniciar sesión y mensajes de ayuda básicos.
2. **Given** que el empleado completa credenciales válidas, **When** envía el formulario, **Then** recibe confirmación visual de acceso concedido y navegación a la vista protegida `/inicio`.
3. **Given** que el empleado envía credenciales inválidas, **When** el sistema responde con rechazo, **Then** se muestra el mensaje genérico `Credenciales inválidas` sin perder los datos no sensibles del formulario.

---

### User Story 2 - Validación y estados de formulario (Priority: P2)

Como empleado, quiero que la pantalla me indique claramente qué falta o qué está mal antes y después del envío para corregirlo rápidamente.

**Why this priority**: Reduce intentos fallidos, frustración y tiempo de acceso.

**Independent Test**: Se valida ejecutando reglas de formulario con campos vacíos, formato inválido y múltiples intentos, verificando mensajes y estados visuales.

**Acceptance Scenarios**:

1. **Given** que el empleado deja campos obligatorios vacíos, **When** intenta enviar, **Then** el sistema impide el envío y muestra errores de validación por campo.
2. **Given** que el empleado corrige un campo con error, **When** sale del campo o vuelve a enviar, **Then** el mensaje de validación se actualiza al estado correcto.

---

### User Story 3 - Cobertura E2E del flujo visual (Priority: P3)

Como equipo de desarrollo, queremos pruebas de frontend automatizadas del login para detectar regresiones de interfaz y flujo de acceso.

**Why this priority**: Asegura calidad continua del flujo crítico de entrada de usuarios.

**Independent Test**: Puede demostrarse de forma independiente ejecutando la suite E2E del login sobre flujos ya implementados, cubriendo éxito, error de credenciales, validación de campos y accesibilidad base.

**Acceptance Scenarios**:

1. **Given** que existe la suite de pruebas del login, **When** se ejecuta en entorno de desarrollo, **Then** valida comportamiento visual y flujo de autenticación en escenarios de éxito y fallo.

### Edge Cases

- Intento de envío repetido mientras la autenticación está en curso: el sistema debe evitar múltiples envíos concurrentes del mismo formulario.
- Error de conectividad o indisponibilidad temporal del servicio: la pantalla debe mostrar un mensaje genérico de fallo y permitir reintento.
- Contraseña con caracteres especiales o espacios: la interfaz debe aceptar entrada válida sin truncamiento visible.
- Sesión ya autenticada al abrir la pantalla de login: el usuario no debe quedar bloqueado en login y debe continuar automáticamente a `/inicio`.

## Requirements _(mandatory)_

### Functional Requirements

- **FR-001**: El sistema MUST proveer una pantalla de login de empleados como punto de entrada principal para recursos protegidos.
- **FR-002**: La pantalla de login MUST incluir, como mínimo, email corporativo, contraseña y acción principal para autenticarse.
- **FR-003**: El sistema MUST validar obligatorios antes del envío y mostrar mensajes de error por campo de forma explícita y comprensible.
- **FR-004**: El sistema MUST mostrar estados visuales diferenciados para autenticación en progreso, autenticación exitosa y autenticación fallida.
- **FR-005**: El sistema MUST mostrar siempre el mensaje genérico `Credenciales inválidas` ante rechazo de autenticación, sin exponer información sensible sobre usuario o contraseña.
- **FR-006**: Tras autenticación exitosa, el sistema MUST redirigir al empleado a la vista protegida `/inicio` como destino post-login único.
- **FR-007**: El sistema MUST mantener consistencia visual y de interacción en todos los estados del login (`INICIAL`, `VALIDACION_ERROR`, `ENVIANDO`, `RECHAZADO`, `ERROR_TECNICO`, `AUTENTICADO`), mostrando siempre la misma estructura base de pantalla (título, campos, acción principal y área de mensajes).
- **FR-008**: El frontend MUST incluir pruebas E2E del login que cubran escenarios de éxito, fallo de credenciales y validación de formulario.
- **FR-009**: Las pruebas E2E MUST ser ejecutables de forma repetible en entorno local sin depender de intervención manual.
- **FR-010**: El flujo visual de login MUST alinearse con la API actual de autenticación: validación de acceso sobre recursos protegidos con HTTP Basic Auth y sin requerir endpoint dedicado de login dentro de este alcance.
- **FR-011**: Las pruebas E2E de esta feature MUST ejecutarse con Cypress contra backend real local (sin mock de autenticación para el flujo principal), validando integración UI + API.
- **FR-012**: La pantalla de login MUST cumplir accesibilidad base WCAG 2.1 AA, incluyendo navegación completa por teclado, etiquetas asociadas a campos y foco visible en controles interactivos.

### Constitution Alignment Requirements _(mandatory)_

- **CA-001**: Esta funcionalidad visual MUST implementarse en Angular 21 LTS y mantener compatibilidad con la versión establecida por la constitución.
- **CA-002**: El mecanismo de autenticación MUST respetar HTTP Basic Auth para el acceso a recursos protegidos, con reglas de acceso explícitas y excepciones públicas justificadas.
- **CA-003**: El flujo de login MUST operar sobre persistencia PostgreSQL y mantener aprovisionamiento reproducible en local/desarrollo mediante Docker para los servicios requeridos.
- **CA-004**: La funcionalidad MUST mantener trazabilidad con el contrato OpenAPI/Swagger publicado para los endpoints involucrados en autenticación y acceso.
- **CA-005**: Toda interacción con endpoints públicos relacionada con este flujo MUST respetar versionado explícito en ruta (`/api/v1/...`).
- **CA-006**: Si el flujo autenticado deriva en consultas de colecciones, estas MUST respetar paginación determinista con máximo 10 instancias por solicitud.
- **CA-007**: La calidad ejecutable MUST incluir pruebas automatizadas del frontend para este flujo, alineadas con la puerta de calidad constitucional.

### Key Entities _(include if feature involves data)_

- **Credencial de Acceso**: Información que el empleado introduce para autenticarse (email corporativo y contraseña), con reglas de obligatoriedad y validación de entrada.
- **Estado de Autenticación de UI**: Representación del estado visible del flujo (inicial, validación, enviando, error, éxito) que guía el comportamiento de la pantalla.
- **Resultado de Inicio de Sesión**: Respuesta funcional del intento de autenticación (aceptado/rechazado/fallo técnico) que determina mensajes, navegación y reintento.

### Assumptions

- El flujo de login se limita a empleados internos del sistema y no incluye registro de nuevos usuarios.
- Este alcance no incorpora endpoint de login dedicado; la interfaz se alinea con autenticación contra recursos protegidos existentes.
- El destino post-login para este alcance es único y corresponde a la ruta protegida `/inicio`.
- La ejecución E2E requiere backend y base de datos locales operativos según la configuración de desarrollo del proyecto.
- La definición visual incluye al menos una pantalla base y los estados esenciales del mismo flujo (error, carga y éxito), sin requerir rediseño del resto de módulos.

## Success Criteria _(mandatory)_

### Measurable Outcomes

- **SC-001**: En al menos 30 ejecuciones E2E consecutivas del escenario de login exitoso, el p95 del tiempo desde envío de formulario hasta navegación a `/inicio` MUST ser menor o igual a 60 segundos.
- **SC-002**: El 100% de intentos con campos obligatorios vacíos muestran validación clara antes de permitir envío.
- **SC-003**: La suite E2E de login ejecutada contra backend real local mantiene una tasa de éxito mínima del 95% en ejecuciones repetidas del entorno de desarrollo.
- **SC-004**: Al menos 90% de una muestra mínima de 10 usuarios de prueba debe calificar con 4/5 o más la claridad del mensaje `Credenciales inválidas` en una encuesta breve posterior a la prueba.
- **SC-005**: El 100% de controles interactivos del login son operables por teclado y presentan foco visible durante pruebas de aceptación.
