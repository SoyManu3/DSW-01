# Runtime Configuration Contract: Frontend API Base URL

## Objetivo

Definir cómo el frontend contenedorizado obtiene la URL base del backend sin recompilar por entorno.

## Variable soportada

- **Nombre**: `API_BASE_URL`
- **Tipo**: string (URL absoluta)
- **Default requerido**: `http://localhost:8080/api/v1`

## Reglas de resolución

1. Si `API_BASE_URL` está presente en entorno del contenedor, debe prevalecer sobre el default.
2. Si no está presente, el frontend debe operar con el default local.
3. El valor efectivo debe estar disponible antes de que la aplicación Angular realice su primera llamada HTTP.
4. El frontend debe aceptar valores con o sin sufijo `/api/v1` y preservar llamadas finales versionadas (`/api/v1/...`).

## Restricciones

- Debe incluir prefijo de versión de API (`/api/v1`).
- No se permiten secretos sensibles en esta variable (solo endpoint base).

## Compatibilidad esperada

- Backend protegido por HTTP Basic Auth: la variable define solo host/path base, no credenciales.
- Compatible con ejecución local Docker Compose y entornos futuros cambiando únicamente la variable.

## Criterios verificables

1. Sin variable definida, las llamadas se dirigen a `http://localhost:8080/api/v1`.
2. Con variable definida (por ejemplo, `http://api:8080/api/v1`), las llamadas se redirigen al nuevo destino sin rebuild.
3. Con valor `http://localhost:8080` (sin sufijo), las llamadas siguen saliendo versionadas a `/api/v1/...`.
