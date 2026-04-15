# Quickstart: CRUD visual de empleados con permisos por rol

## Prerrequisitos

- Java 17
- Maven 3.9+
- Docker + Docker Compose
- Node.js LTS compatible con Angular 21

## 1) Levantar backend y base de datos

1. Iniciar PostgreSQL con Docker Compose.
   - `docker compose -f docker/docker-compose.yml up -d postgres`
2. Iniciar backend Spring Boot.
   - `mvn spring-boot:run`
3. Verificar disponibilidad de API versionada.
   - `curl -i http://localhost:8080/api/v1/empleados?page=0`

## 2) Levantar frontend

1. Instalar dependencias.
   - `npm --prefix frontend install`
2. Iniciar Angular.
   - `npm --prefix frontend run start`
3. Abrir aplicación.
   - `http://localhost:4200`

## 3) Validar flujo por rol

### Admin

1. Login con usuario de rol `ADMIN`.
2. Abrir módulo de empleados.
3. Confirmar que crear/editar/eliminar están habilitados.
4. Ejecutar alta, edición y eliminación y verificar refresco del listado.

### Empleado

1. Login con usuario de rol `EMPLEADO`.
2. Abrir módulo de empleados.
3. Confirmar modo solo lectura (sin botones de crear/editar/eliminar).
4. Navegar manualmente a `/empleados/nuevo` o `/empleados/<clave>/editar` y verificar redirección a `/empleados` con mensaje de permisos insuficientes.

## 4) Pruebas automatizadas recomendadas

1. Ejecutar pruebas frontend unitarias/componentes.
   - `npm --prefix frontend run test`
2. Ejecutar E2E de permisos por rol.
   - `export CYPRESS_adminEmail="admin@test.com"`
   - `export CYPRESS_adminPassword="clave123"`
   - `export CYPRESS_empleadoEmail="usuario@test.com"`
   - `export CYPRESS_empleadoPassword="clave123"`
   - `npm --prefix frontend run e2e`
3. Ejecutar pruebas backend relevantes de seguridad/integración.
   - `mvn test`

## 5) Evidencias de aceptación

- Captura de sesión admin con acciones CRUD habilitadas.
- Captura de sesión empleado en modo solo lectura.
- Evidencia de redirección por acceso no autorizado vía URL.
- Reporte de pruebas con escenarios de matriz de permisos en verde.
