# Research: Contenedor Docker Frontend con Nginx

## Decision 1: Estrategia de imagen frontend

- **Decision**: Usar Docker multi-stage (`node:20` para build Angular y `nginx:alpine` para runtime estático).
- **Rationale**: Reduce tamaño final de imagen, separa toolchain de build del runtime y simplifica seguridad operativa.
- **Alternatives considered**:
  - Ejecutar `ng serve` en contenedor: descartado por ser modo desarrollo y no runtime estable.
  - Imagen única con Node + Nginx: descartado por mayor superficie y peso.

## Decision 2: Configuración runtime de API

- **Decision**: Exponer `API_BASE_URL` por variable de entorno y materializarla en runtime (archivo JS de configuración servido por Nginx), con default `http://localhost:8080/api/v1`.
- **Rationale**: Permite reutilizar la misma imagen en distintos entornos sin recompilar frontend por cada URL.
- **Alternatives considered**:
  - Hardcode en `environment.ts`: descartado por rigidez y riesgo de drift entre ambientes.
  - Build arg obligatorio para cada entorno: descartado por mayor fricción en despliegue local.

## Decision 3: Integración Docker Compose

- **Decision**: Agregar servicio `frontend` en `docker/docker-compose.yml` con mapeo `4200:80`, dependencia de `api` y red compartida por default Compose.
- **Rationale**: Cumple clarificación aprobada y mantiene arranque unificado del stack.
- **Alternatives considered**:
  - Compose separado para frontend: descartado por duplicidad operativa.
  - Publicar en puerto 80 host: descartado por conflictos frecuentes en equipos locales.

## Decision 4: Healthcheck del servicio frontend

- **Decision**: Configurar `healthcheck` HTTP del contenedor frontend contra `/`.
- **Rationale**: Detecta fallas reales de disponibilidad de Nginx y artefactos servidos.
- **Alternatives considered**:
  - Sin healthcheck: descartado por baja observabilidad del estado efectivo.
  - Healthcheck solo en CI: descartado por menor valor en diagnóstico local.

## Decision 5: Soporte SPA en Nginx

- **Decision**: Configurar `try_files` para fallback a `index.html` en rutas de aplicación.
- **Rationale**: Evita errores 404 al refrescar rutas internas (`/login`, `/departamentos`, etc.).
- **Alternatives considered**:
  - Manejo exclusivo de rutas en cliente sin fallback servidor: descartado por fallas de recarga directa.
  - Reescrituras por ruta específica: descartado por mantenimiento complejo.

## Decision 6: Estrategia de validación

- **Decision**: Validar con smoke tests de build y disponibilidad (`docker compose up --build`, respuesta HTTP 200 y estado healthy).
- **Rationale**: Cubre los requisitos funcionales de la feature sin sobrecargar con pruebas no relacionadas.
- **Alternatives considered**:
  - Solo validación manual en navegador: descartado por baja repetibilidad.
  - Suite E2E completa para esta feature: descartado por alcance operacional, no funcional.

## Decision 7: Compatibilidad de `API_BASE_URL` con y sin sufijo `/api/v1`

- **Decision**: Normalizar en frontend el valor de `API_BASE_URL` para soportar entradas con o sin sufijo `/api/v1`, manteniendo llamadas finales versionadas.
- **Rationale**: Permite cumplir el contrato de configuración runtime sin romper servicios existentes que ya concatenan rutas `/api/v1/...`.
- **Alternatives considered**:
  - Reescribir todos los servicios para usar base URL ya versionada: descartado por impacto transversal mayor.
  - Forzar solo formato con `/api/v1`: descartado por menor tolerancia operativa.

## Decision 8: Verificaciones constitucionales explícitas en fase de polish

- **Decision**: Añadir validaciones dedicadas para disponibilidad OpenAPI/Swagger, no-regresión de paginación de 10 y preservación de HTTP Basic Auth.
- **Rationale**: Alinea evidencias de implementación con la constitución y evita falsos cierres por cobertura implícita.
- **Alternatives considered**:
  - Confiar en validaciones manuales no registradas: descartado por baja trazabilidad.
