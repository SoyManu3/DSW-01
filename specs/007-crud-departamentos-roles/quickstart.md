# Quickstart: CRUD visual de departamentos con permisos por rol

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
3. Verificar endpoint de departamentos versionado.
   - `curl -i http://localhost:8080/api/v1/departamentos?page=0`

## 2) Levantar frontend

1. Instalar dependencias.
   - `npm --prefix frontend install`
2. Iniciar Angular.
   - `npm --prefix frontend run start`
3. Abrir aplicación.
   - `http://localhost:4200`

## 3) Validar flujo por rol

### Admin (`ADMIN`)

1. Iniciar sesión con credenciales admin.
2. Entrar al módulo de departamentos.
3. Confirmar acciones CRUD habilitadas.
4. Ejecutar crear, editar y eliminar; verificar refresco de listado.

### Empleado (`EMPLEADO`)

1. Iniciar sesión con credenciales de empleado.
2. Entrar al módulo de departamentos.
3. Confirmar que acciones CRUD están visibles y deshabilitadas.
4. Navegar manualmente a `/departamentos/nuevo` o `/departamentos/1/editar` y verificar redirección a `/departamentos` con `No tienes permisos para esta acción.`.

### Sesión inválida o expirada

1. Limpiar sesión (logout o borrado de storage) y abrir `/departamentos`.
2. Verificar redirección a `/login` con `Sesión expirada o no autenticada.`.

## 4) Pruebas automatizadas recomendadas

1. Ejecutar pruebas frontend (guards/servicios/componentes).
   - `npm --prefix frontend run test`
2. Ejecutar E2E de permisos por rol.
   - `npm --prefix frontend run e2e`
3. Ejecutar pruebas backend relevantes de seguridad/contrato.
   - `mvn test`

## 5) Evidencias de aceptación

- Evidencia de sesión admin con CRUD habilitado y operaciones exitosas.
- Evidencia de sesión empleado con acciones visibles deshabilitadas.
- Evidencia de redirección por intento de acceso a rutas de escritura sin rol admin.
- Evidencia de redirección al login por sesión ausente/expirada.

## 6) Protocolo de medición SC-003 (usabilidad)

1. Ejecutar una prueba moderada con 20 participantes representativos.
   - 10 sesiones con rol `ADMIN`
   - 10 sesiones con rol `EMPLEADO`
2. En cada ejecución, medir con cronómetro el tiempo desde carga de `/departamentos` hasta la identificación verbal/escrita de permisos.
3. Registrar cada resultado en `specs/007-crud-departamentos-roles/checklists/usabilidad-sc003.md`.
4. Considerar `PASS` de SC-003 cuando al menos 19/20 ejecuciones (95%) sean correctas y en <= 10 segundos.
