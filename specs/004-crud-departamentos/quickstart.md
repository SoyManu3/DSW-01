# Quickstart - CRUD de Departamentos y Relación con Empleados

## Prerrequisitos

- Java 17
- Docker y Docker Compose
- Maven

## 1) Levantar PostgreSQL local

```bash
docker compose -f docker/docker-compose.yml up -d postgres
```

## 2) Configurar variables de entorno (si ejecutas API local)

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleadosdb
export SPRING_DATASOURCE_USERNAME=empleados
export SPRING_DATASOURCE_PASSWORD=empleados
```

## 3) Ejecutar la API

```bash
./mvnw spring-boot:run
```

## 4) Verificar documentación

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Colección Postman: `postman/departamentos.postman_collection.json`

## 5) Probar CRUD de departamentos (Basic Auth obligatorio)

### 5.1 Crear departamento

Request:

```http
POST /api/v1/departamentos
Authorization: Basic <credenciales>
Content-Type: application/json

{
  "nombre": "Ventas"
}
```

Resultado esperado: `201 Created` con `id`, `nombre` y `empleadosCount=0`.

### 5.2 Listar departamentos

Request:

```http
GET /api/v1/departamentos?page=0
Authorization: Basic <credenciales>
```

Resultado esperado: `200 OK`, máximo 10 elementos, orden por `id` ascendente.

### 5.3 Obtener detalle de departamento

Request:

```http
GET /api/v1/departamentos/{id}
Authorization: Basic <credenciales>
```

Resultado esperado: `200 OK` con datos del departamento + contador de empleados.

### 5.4 Actualizar departamento

Request:

```http
PUT /api/v1/departamentos/{id}
Authorization: Basic <credenciales>
Content-Type: application/json

{
  "nombre": "ventas"
}
```

Resultado esperado: `200 OK` (se permite por unicidad case-sensitive si no existe exacto `ventas`).

### 5.5 Eliminar departamento

Request:

```http
DELETE /api/v1/departamentos/{id}
Authorization: Basic <credenciales>
```

Resultado esperado:

- `204 No Content` si no tiene empleados asociados.
- Error de negocio (p.ej. `409`) si tiene empleados asociados.

## 6) Probar empleados por departamento

Request:

```http
GET /api/v1/departamentos/{id}/empleados?page=0
Authorization: Basic <credenciales>
```

Resultado esperado: `200 OK`, máximo 10 empleados, orden por `id` ascendente.

## 7) Casos de validación clave

- `nombre` vacío o en blanco -> `400`.
- `nombre` con más de 100 caracteres -> `400`.
- `nombre` duplicado exacto -> `409`.
- Empleado sin departamento -> rechazo de validación/regla de negocio.

## 8) Ejecutar pruebas

```bash
./mvnw test
```

## 9) Evidencia de validación (2026-03-19)

- Validación funcional manual del quickstart (instancia local en `http://localhost:8081`):
  - `POST /api/v1/departamentos` -> `201`
  - `GET /api/v1/departamentos?page=0` -> `200`
  - `GET /api/v1/departamentos/{id}` -> `200`
  - `PUT /api/v1/departamentos/{id}` -> `200`
  - `GET /api/v1/departamentos/{id}/empleados?page=0` -> `200`
  - `DELETE /api/v1/departamentos/{id}` (sin empleados asociados) -> `204`
  - Validaciones de nombre:
    - nombre en blanco -> `400`
    - nombre > 100 caracteres -> `400`
    - nombre duplicado exacto -> primer `POST` `201`, segundo `POST` `409`
- Validación funcional automatizada complementaria:
  - `DepartamentoCrudIntegrationTest`
  - `DepartamentoPaginationIntegrationTest`
  - `DepartamentoDetailIntegrationTest`
  - `DepartamentoEmpleadosPaginationIntegrationTest`
  - `DepartamentoDeleteGuardIntegrationTest`
  - `DepartamentoErrorPayloadIntegrationTest`
  - `EmpleadoDepartamentoAssignmentIntegrationTest`
  - Resultado de ejecución: **7 passed, 0 failed**.
- Medición p95 (< 2s):
  - Prueba ejecutada: `EmpleadoAuthenticationIntegrationTest.autenticacionValida_p95DebeSerMenorA2SegundosEn10Ejecuciones`.
  - Resultado de ejecución: **1 passed, 0 failed** (la prueba valida internamente p95 < 2000 ms en 10 ejecuciones).
- Nota de entorno:
  - El puerto `8080` tenía una instancia previa (contrato antiguo). La validación manual se ejecutó contra la instancia de la rama actual levantada en `8081`.

## Resultado esperado

- CRUD completo de departamentos bajo `/api/v1/departamentos`.
- Relación 1:N activa entre departamentos y empleados.
- Paginación fija de 10 y orden determinista por `id` en colecciones.
- Seguridad Basic Auth aplicada y contrato OpenAPI/Swagger actualizado.
