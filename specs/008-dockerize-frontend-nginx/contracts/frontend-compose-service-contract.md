# Docker Compose Service Contract: Frontend Nginx

## Objetivo

Definir el contrato operativo mínimo del servicio `frontend` integrado al stack local con Docker Compose.

## Servicio

- **Nombre**: `frontend`
- **Imagen/Build**: construida desde código fuente de `frontend/`.
- **Propósito**: servir artefactos estáticos de Angular mediante Nginx.

## Reglas de exposición

- **Mapeo de puertos obligatorio**: `4200:80`.
- **URL de acceso esperada**: `http://localhost:4200`.

## Dependencias

- Debe declarar dependencia de `api` para coordinar arranque del stack.
- Debe compartir red Compose para resolver backend por nombre de servicio cuando aplique.
- Debe permitir sobreescritura de `API_BASE_URL` por variable de entorno sin reconstruir imagen.

## Healthcheck

- Debe existir `healthcheck` HTTP del contenedor frontend.
- Ruta objetivo: `/`.
- Estado esperado: respuesta exitosa (2xx/3xx según implementación del comando de chequeo).

## Comportamiento ante fallas

- Si el puerto 4200 está ocupado, el servicio no inicia y Compose debe reportar error observable.
- Si faltan artefactos estáticos, el healthcheck debe degradar a `unhealthy`.

## Invariantes verificables

1. Con `docker compose up --build`, el servicio `frontend` alcanza estado healthy.
2. `http://localhost:4200` responde contenido HTML de la app.
3. El stack mantiene operativos `postgres` y `api` sin regresión por la incorporación del servicio frontend.
4. Swagger/OpenAPI del backend permanece accesible en `http://localhost:8080/v3/api-docs`.
5. Endpoints de colección del backend mantienen paginación de 10 elementos por consulta.
