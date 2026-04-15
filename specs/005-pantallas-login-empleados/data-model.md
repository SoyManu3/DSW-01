# Data Model: Pantallas de Login de Empleados

## 1. CredencialAccesoUI

- **Descripción**: Datos ingresados por el empleado en el formulario de login.
- **Campos**:
  - `emailCorporativo` (string, obligatorio, formato email)
  - `contrasena` (string, obligatorio, no vacío)
- **Reglas de validación**:
  - Ambos campos son requeridos antes de permitir envío.
  - `emailCorporativo` debe cumplir formato de correo válido.
  - `contrasena` acepta caracteres especiales y espacios sin truncamiento visual.

## 2. EstadoAutenticacionUI

- **Descripción**: Estado de interacción visible durante el ciclo de login.
- **Estados permitidos**:
  - `INICIAL`
  - `VALIDACION_ERROR`
  - `ENVIANDO`
  - `AUTENTICADO`
  - `RECHAZADO`
  - `ERROR_TECNICO`
- **Transiciones**:
  - `INICIAL -> VALIDACION_ERROR` (si hay campos inválidos)
  - `INICIAL -> ENVIANDO` (envío válido)
  - `VALIDACION_ERROR -> ENVIANDO` (corrección + reintento)
  - `ENVIANDO -> AUTENTICADO` (credenciales válidas)
  - `ENVIANDO -> RECHAZADO` (credenciales inválidas)
  - `ENVIANDO -> ERROR_TECNICO` (falla de conectividad/servicio)
  - `RECHAZADO -> ENVIANDO` (nuevo intento)
  - `ERROR_TECNICO -> ENVIANDO` (reintento)

## 3. ResultadoLogin

- **Descripción**: Resultado funcional que retorna la integración UI+API al intentar acceso.
- **Campos**:
  - `resultado` (enum: `ACEPTADO`, `RECHAZADO`, `FALLO_TECNICO`)
  - `mensajeUsuario` (string)
  - `rutaDestino` (string opcional; para éxito debe ser `/inicio`)
- **Reglas**:
  - Si `resultado=RECHAZADO`, `mensajeUsuario` debe ser exactamente `Credenciales inválidas`.
  - Si `resultado=ACEPTADO`, `rutaDestino` debe ser `/inicio`.

## Relaciones

- `CredencialAccesoUI` alimenta un intento que produce `ResultadoLogin`.
- `ResultadoLogin` determina la actualización de `EstadoAutenticacionUI`.
- `EstadoAutenticacionUI` gobierna feedback visual y habilitación de acciones en la pantalla.
