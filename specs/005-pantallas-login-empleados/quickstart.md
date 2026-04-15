# Quickstart: Pantallas de Login de Empleados

## Prerrequisitos

- Java 17
- Maven 3.9+
- Docker y Docker Compose
- Node.js LTS compatible con Angular 21 (recomendado: 20.x)

## 1) Levantar backend y base de datos

1. Iniciar PostgreSQL local con Docker Compose del proyecto.
   - `docker compose -f docker/docker-compose.yml up -d postgres`
2. Iniciar API Spring Boot.
   - `mvn spring-boot:run`
3. Verificar que endpoints protegidos en `/api/v1/...` responden con desafío HTTP Basic cuando no hay credenciales.
   - `curl -i http://localhost:8080/api/v1/empleados?page=0`

## 2) Levantar frontend Angular 21

1. Instalar dependencias en `frontend/`.
   - `npm --prefix frontend install`
2. Iniciar frontend.
   - `npm --prefix frontend run start`
3. Abrir `http://localhost:4200/login`.

## 3) Validar comportamiento funcional

1. Login válido redirige a `/inicio`.
2. Login inválido muestra `Credenciales inválidas`.
3. Campos inválidos no permiten envío y muestran errores por campo.
4. Reintento disponible tras rechazo o fallo técnico.

## 4) Ejecutar pruebas Cypress E2E (backend real)

1. Mantener backend y BD activos localmente.
2. Ejecutar Cypress contra la aplicación frontend.
   - `npm --prefix frontend run e2e`
3. Verificar cobertura de:
   - éxito con redirección a `/inicio`
   - fallo de credenciales con mensaje genérico
   - validación de formulario
   - navegación por teclado y foco visible

> Nota: si el binario de Cypress no está instalado por restricciones de red,
> ejecutar `CYPRESS_INSTALL_BINARY=0 npm --prefix frontend install` y dejar
> pendiente `cypress install` en un entorno con acceso a descarga.

## 5) Evidencias de aceptación

- Captura o reporte de ejecución Cypress en verde.
- Evidencia funcional de redirección a `/inicio`.
- Evidencia de accesibilidad base (teclado, labels, foco visible).
