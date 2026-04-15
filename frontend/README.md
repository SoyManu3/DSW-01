# Frontend Empleados (Angular 21)

Frontend para autenticación y gestión visual de empleados, alineado con API protegida por HTTP Basic Auth.

## Requisitos

- Node.js 20.19.0+
- npm 10+
- Backend disponible en `http://localhost:8080`

## Scripts principales

```bash
npm install
npm run start
npm run build
npm run test
```

## Ejecución con Docker + Nginx

Desde la raíz del repositorio:

```bash
docker compose -f docker/docker-compose.yml up --build -d frontend
```

Acceso frontend:

- `http://localhost:4200`

Configuración runtime soportada:

- `API_BASE_URL` (default: `http://localhost:8080/api/v1`)
- Se acepta valor con o sin sufijo `/api/v1`.

Pruebas automatizadas de contenedor:

```bash
bash docker/tests/run-frontend-container-tests.sh
```

## Flujo funcional implementado

- Ruta pública: `/login`
- Ruta protegida: `/inicio`
- Ruta protegida de listado: `/empleados`
- Ruta protegida de detalle: `/empleados/:clave`
- Rutas de escritura solo admin: `/empleados/nuevo`, `/empleados/:clave/editar`
- Login con `email corporativo + contraseña`
- Mensaje de error único: `Credenciales inválidas`
- Estado de bloqueo de botón durante autenticación
- Guard para sesión existente en rutas protegidas
- Guard de rol para escritura y redirección con mensaje `No tienes permisos para esta acción.`

## E2E con Cypress

```bash
npm run e2e
```

Variables recomendadas para escenarios reales:

```bash
export CYPRESS_adminEmail="admin@test.com"
export CYPRESS_adminPassword="clave123"
export CYPRESS_empleadoEmail="usuario@test.com"
export CYPRESS_empleadoPassword="clave123"
export CYPRESS_loginEmail="$CYPRESS_adminEmail"
export CYPRESS_loginPassword="$CYPRESS_adminPassword"
```

Si el entorno no permite descargar el binario de Cypress desde Internet, instala dependencias con:

```bash
CYPRESS_INSTALL_BINARY=0 npm install
```

En ese caso, la ejecución E2E quedará pendiente hasta disponer del binario (`cypress install`).
