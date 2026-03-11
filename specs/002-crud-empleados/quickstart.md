# Quickstart - CRUD de empleados

## Prerrequisitos

- Java 17
- Docker y Docker Compose
- Maven o Gradle

## 1) Levantar PostgreSQL local

```bash
docker compose -f docker/docker-compose.yml up -d
```

## 1.1) Levantar API + PostgreSQL en contenedores (opcional)

```bash
docker compose -f docker/docker-compose.yml up -d --build
```

## 2) Configurar variables de entorno

Ejemplo:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/empleadosdb
export SPRING_DATASOURCE_USERNAME=empleados
export SPRING_DATASOURCE_PASSWORD=empleados
```

## 3) Ejecutar aplicación

Con Maven:

```bash
./mvnw spring-boot:run
```

Con Gradle:

```bash
./gradlew bootRun
```

Si ejecutas con contenedores (paso 1.1), no necesitas este paso.

## 4) Verificar documentación Swagger

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## 5) Probar endpoints CRUD (requieren Basic Auth)

- `POST /api/v1/empleados`
- `GET /api/v1/empleados?page=0` (paginado, tamaño fijo de 10)
- `GET /api/v1/empleados/{clave}`
- `PUT /api/v1/empleados/{clave}`
- `DELETE /api/v1/empleados/{clave}`

### Ejemplo de alta (sin `clave` en payload)

```json
{
  "nombre": "Ana Ruiz",
  "direccion": "Av. Principal 123",
  "telefono": "5557777"
}
```

Respuesta esperada:

- `201 Created`
- Campo `clave` autogenerado con formato `EMP-<n>` (ej. `EMP-1`)

### Ejemplo de listado paginado

- Solicitud: `GET /api/v1/empleados?page=0`
- Respuesta esperada:
  - `200 OK`
  - `size = 10`
  - `content` con hasta 10 empleados

## 6) Ejecutar pruebas

```bash
./mvnw test
```

## Resultado esperado

- Base PostgreSQL operativa en Docker.
- API disponible con endpoints CRUD de empleados.
- Swagger accesible en desarrollo.
- Validaciones de longitud y PK compuesta aplicadas.

## Validación ejecutada

- Fecha: 2026-02-26
- Comando: `mvn -q test`
- Resultado: `EXIT_CODE=0`
