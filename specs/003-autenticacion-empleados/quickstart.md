# Quickstart - Autenticación de empleados

## Prerrequisitos

- Java 17
- Docker y Docker Compose
- Maven

## 1) Levantar infraestructura local

Base de datos PostgreSQL:

```bash
docker compose -f docker/docker-compose.yml up -d postgres
```

API + base de datos (opcional en contenedor):

```bash
docker compose -f docker/docker-compose.yml up -d --build
```

## 2) Configurar variables de entorno (si ejecutas API local)

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleadosdb
export SPRING_DATASOURCE_USERNAME=empleados
export SPRING_DATASOURCE_PASSWORD=empleados
```

Credenciales iniciales para bootstrap administrativo (solo desarrollo):

```bash
export APP_BASIC_AUTH_USER=admin
export APP_BASIC_AUTH_PASSWORD=admin123
```

## 3) Ejecutar aplicación

```bash
./mvnw spring-boot:run
```

Si ejecutas API en Docker (paso 1 opcional), este paso no es necesario.

## 4) Verificar documentación

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 5) Flujo de autenticación esperado

- No existe endpoint de login dedicado.
- La autenticación se valida al consumir cualquier endpoint protegido con Basic Auth.
- Username = `correo` del empleado.
- Password = contraseña del empleado.

## 6) Probar políticas de seguridad

### 6.1 Credenciales válidas

- Consumir `GET /api/v1/empleados?page=0` con Basic Auth válido.
- Resultado esperado: `200 OK` (máximo 10 elementos por respuesta).

### 6.2 Credenciales inválidas

- Repetir solicitud con contraseña incorrecta.
- Resultado esperado: `401 Unauthorized`.

### 6.3 Bloqueo temporal

- Enviar 5 intentos fallidos consecutivos para el mismo correo.
- Resultado esperado: cuenta bloqueada 15 minutos; solicitudes siguientes se rechazan.
- Tras 15 minutos, el desbloqueo es automático y el contador de fallos se reinicia.

## 7) Ejecutar pruebas

```bash
./mvnw test
```

## 8) Validar SC-003 (rendimiento de autenticación)

- Ejecutar la prueba de integración `autenticacionValida_p95DebeSerMenorA2SegundosEn10Ejecuciones`.
- Criterio de aceptación: p95 de 10 autenticaciones válidas < 2 segundos.
- Referencia de ejecución: `src/test/java/com/example/empleados/integration/EmpleadoAuthenticationIntegrationTest.java`.

### Evidencia esperada

- Resultado verde en la prueba de integración de latencia.
- Si falla, revisar recursos de entorno local (Docker/CPU) y repetir con entorno estabilizado.

## Resultado esperado

- Endpoints `/api/v1/empleados/**` protegidos con Basic Auth usando correo + contraseña.
- Regla de contraseña mínima aplicada en altas/actualizaciones.
- Bloqueo temporal tras 5 fallos consecutivos con desbloqueo automático.
- Contrato OpenAPI y Swagger reflejando cambios de autenticación y payloads.
