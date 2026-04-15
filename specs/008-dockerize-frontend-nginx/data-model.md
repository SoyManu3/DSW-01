# Data Model: Contenedor Docker Frontend con Nginx

## 1. FrontendImageArtifact

- **Descripción**: Artefacto de imagen Docker resultante del build del frontend Angular para servir contenido estático.
- **Campos**:
  - `imageName` (string, obligatorio) — nombre/tag de imagen.
  - `buildContext` (string, obligatorio) — contexto de build del frontend.
  - `dockerfilePath` (string, obligatorio) — ubicación del Dockerfile frontend.
  - `staticOutputPath` (string, obligatorio) — ruta de artefactos compilados copiados a Nginx.
- **Validaciones**:
  - El build debe completarse sin errores.
  - `staticOutputPath` debe existir al finalizar stage de build.

## 2. FrontendRuntimeConfig

- **Descripción**: Configuración de runtime inyectada al frontend servido por Nginx.
- **Campos**:
  - `API_BASE_URL` (string URL, obligatorio) — base URL de API, default `http://localhost:8080/api/v1`.
  - `servedPort` (integer, fijo) — puerto interno Nginx `80`.
- **Validaciones**:
  - `API_BASE_URL` debe incluir esquema HTTP/HTTPS válido.
  - Si no se informa `API_BASE_URL`, se aplica el valor por defecto local.

## 3. FrontendComposeService

- **Descripción**: Definición del servicio frontend dentro de Docker Compose.
- **Campos**:
  - `serviceName` (string, fijo) — `frontend`.
  - `portMapping` (string, fijo) — `4200:80`.
  - `dependsOn` (array, obligatorio) — dependencia declarada de `api`.
  - `healthcheck` (object, obligatorio) — chequeo HTTP a `/`.
  - `environment` (map, opcional) — variables runtime incluyendo `API_BASE_URL`.
- **Validaciones**:
  - `portMapping` debe respetar host `4200`.
  - `healthcheck` debe fallar si Nginx no responde correctamente.

## 4. FrontendServiceHealth

- **Descripción**: Estado observable del servicio frontend en ejecución Compose.
- **Estados**:
  - `STARTING`
  - `HEALTHY`
  - `UNHEALTHY`
  - `STOPPED`
- **Transiciones**:
  - `STARTING -> HEALTHY` cuando healthcheck HTTP devuelve éxito consecutivo.
  - `STARTING -> UNHEALTHY` si healthcheck excede reintentos.
  - `HEALTHY -> UNHEALTHY` ante caída de Nginx/artefactos no servibles.
  - `* -> STOPPED` cuando el servicio se detiene manualmente o por error fatal.
