# UI Contract: Login de Empleados

## Objetivo

Definir el contrato funcional de la interfaz de login para empleados, alineado con autenticación HTTP Basic del backend existente.

## Entradas de UI

- `emailCorporativo` (campo de texto tipo email)
- `contrasena` (campo password)
- Acción principal: `Iniciar sesión`

## Reglas de interacción

1. No se permite envío si faltan campos obligatorios.
2. El formulario debe permitir navegación por teclado completa.
3. El foco visible debe ser perceptible en todos los controles interactivos.
4. Mientras se procesa autenticación, se deshabilita el envío repetido.

## Salidas esperadas

- **Éxito**: navegación a `/inicio`.
- **Rechazo autenticación**: mensaje único `Credenciales inválidas`.
- **Fallo técnico**: mensaje genérico de indisponibilidad y opción de reintento.

## Integración con API

- Mecanismo: `HTTP Basic Auth`.
- Alcance: no se define endpoint dedicado de login en esta feature.
- Recursos protegidos: se validan contra endpoints versionados existentes (`/api/v1/...`), usando `GET /api/v1/empleados?page=0` como verificación de autenticación para el flujo de login.

## Criterios de prueba E2E (Cypress)

- Escenario 1: login exitoso con redirección a `/inicio`.
- Escenario 2: credenciales inválidas muestran `Credenciales inválidas`.
- Escenario 3: validaciones de formulario bloquean envío inválido.
- Escenario 4: flujo operable por teclado con foco visible.

## Evidencia de consistencia FR-007

- Estados UI cubiertos: `INICIAL`, `VALIDACION_ERROR`, `ENVIANDO`, `RECHAZADO`, `ERROR_TECNICO`, `AUTENTICADO`.
- Estructura visual base preservada en todos los estados: título, campos, acción principal, área de mensajes.
