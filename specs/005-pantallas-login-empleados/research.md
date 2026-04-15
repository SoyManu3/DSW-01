# Research: Pantallas de Login de Empleados

## Decision 1: Framework visual y versionado

- **Decision**: Implementar frontend en Angular 21 LTS.
- **Rationale**: La constitución del proyecto exige Angular 21 LTS para toda capa visual principal.
- **Alternatives considered**:
  - Angular 22 LTS: descartado por no alinearse con la versión constitucional vigente.
  - React/Vue: descartado por violar la homogeneidad tecnológica requerida.

## Decision 2: Estrategia de autenticación en UI

- **Decision**: Alinear el flujo visual con HTTP Basic Auth del backend existente, sin endpoint dedicado de login.
- **Rationale**: La API actual valida credenciales en acceso a recursos protegidos; esto minimiza cambios de contrato y preserva seguridad vigente.
- **Alternatives considered**:
  - Crear endpoint `/api/v1/auth/login`: descartado por estar fuera de alcance y contradecir la especificación de autenticación ya aprobada.
  - Migrar a token JWT en esta feature: descartado por introducir cambio arquitectónico mayor no solicitado.

## Decision 3: Identificador de acceso

- **Decision**: Usar email corporativo + contraseña en la pantalla de login.
- **Rationale**: Coherencia con el modelo de empleado y con el comportamiento autenticado esperado por la API.
- **Alternatives considered**:
  - Username genérico: descartado por ambigüedad funcional.
  - Código de empleado: descartado por menor alineación con validaciones de formato actuales.

## Decision 4: Destino post-login

- **Decision**: Redirección única a `/inicio` tras autenticación exitosa.
- **Rationale**: Reduce complejidad del primer release y facilita pruebas E2E deterministas.
- **Alternatives considered**:
  - Última ruta visitada: descartado por agregar estado de navegación adicional.
  - Redirección por rol: descartado por requerir reglas de autorización no incluidas en el alcance.

## Decision 5: Mensajería de error de autenticación

- **Decision**: Mostrar siempre mensaje genérico `Credenciales inválidas`.
- **Rationale**: Evita filtración de información sensible sobre existencia de usuario o causa exacta de fallo.
- **Alternatives considered**:
  - Mensajes diferenciados por tipo de error: descartado por mayor riesgo de enumeración de cuentas.

## Decision 6: Estrategia de pruebas frontend

- **Decision**: Cypress E2E contra backend real local.
- **Rationale**: Asegura verificación real de integración UI + API + seguridad HTTP Basic.
- **Alternatives considered**:
  - Mock completo de API: descartado por riesgo de falsos positivos de integración.
  - Estrategia híbrida: pospuesta para una iteración posterior; no necesaria para MVP.

## Decision 7: Accesibilidad mínima obligatoria

- **Decision**: Exigir accesibilidad base WCAG 2.1 AA (teclado, labels, foco visible).
- **Rationale**: Permite calidad de uso verificable desde el MVP y evita deuda de accesibilidad temprana.
- **Alternatives considered**:
  - Sin requisito explícito: descartado por baja calidad UX.
  - Requisitos avanzados adicionales (`aria-live` en todos los estados): pospuesto para futuras mejoras.
