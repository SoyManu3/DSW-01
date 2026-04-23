# Configuración de SonarCloud

Este documento describe cómo configurar e implementar el análisis de código con SonarCloud en el proyecto.

## Requisitos previos

1. **Cuenta en SonarCloud**: Crear una cuenta en https://sonarcloud.io/
2. **GitHub Token**: Se crea automáticamente en las secrets de GitHub
3. **SonarCloud Token**: Generar en SonarCloud

## Pasos de Configuración

### 1. Crear cuenta en SonarCloud

- Visita https://sonarcloud.io/
- Inicia sesión con GitHub
- Autoriza SonarCloud a acceder a tus repositorios

### 2. Crear proyectos en SonarCloud

#### Backend

- Nombre: `dsw2-practica01-backend`
- Tipo: Java / Maven
- Organización: Tu nombre de usuario o organización

#### Frontend

- Nombre: `dsw2-practica01-frontend`
- Tipo: TypeScript / JavaScript
- Organización: Tu nombre de usuario o organización

### 3. Generar token en SonarCloud

1. Accede a Security > Tokens
2. Genera un nuevo token con alcance global
3. Guarda el token

### 4. Configurar secretos en GitHub

1. Ve a Settings > Secrets and variables > Actions
2. Crea un nuevo secreto llamado `SONAR_TOKEN`
3. Pega el token generado en SonarCloud

### 5. Actualizar el workflow

En el archivo `.github/workflows/sonarcloud.yml`, reemplaza `tu-organizacion` con:

- Tu nombre de usuario en SonarCloud (para usuario personal)
- Tu nombre de organización (si usas una organización)

```bash
# Ejemplo para usuario personal:
-Dsonar.organization=tu-github-username

# Ejemplo para organización:
-Dsonar.organization=tu-organizacion-github
```

## Archivos de Configuración

### Backend: `sonar-project.properties`

Configuración para análisis del backend:

- Fuentes: `src/main/java`
- Tests: `src/test/java`
- Cobertura: JaCoCo (generada en `target/site/jacoco/jacoco.xml`)
- Reporte de tests: JUnit

### Frontend: `frontend/sonar-project.properties`

Configuración para análisis del frontend:

- Fuentes: `frontend/src`
- Tests: `**/*.spec.ts`
- Cobertura: LCOV (generada en `frontend/coverage/lcov.info`)

## Workflow de CI/CD

El workflow se ejecuta automáticamente cuando:

### En Pull Requests

Se ejecuta contra `develop`, `master` o `release`

### En Push

Se ejecuta en `develop` y `master`

### Lo que hace el workflow

#### Backend

1. Checkout del código con historial completo
2. Setup de Java 17 y Maven
3. Ejecución de tests con `mvn verify`
4. Generación de reportes de cobertura con JaCoCo
5. Envío de análisis a SonarCloud

#### Frontend

1. Checkout del código con historial completo
2. Setup de Node.js 22
3. Instalación de dependencias
4. Ejecución de tests con cobertura
5. Envío de análisis a SonarCloud

## Monitoreo de Calidad

Después de cada PR o push, puedes:

1. Ver el análisis en https://sonarcloud.io/projects
2. Revisar métricas de cobertura
3. Identificar problemas de calidad
4. Ver hotspots de seguridad

## Troubleshooting

### El workflow no se ejecuta

- Verifica que `SONAR_TOKEN` esté configurado en GitHub Secrets
- Asegúrate de que los project keys coinciden en SonarCloud y el workflow

### No se genera cobertura

- Backend: Verifica que `target/site/jacoco/jacoco.xml` se genera después de `mvn verify`
- Frontend: Ejecuta `npm test -- --coverage` manualmente para verificar

### Error de organización

- Verifica que el valor de `sonar.organization` coincida exactamente con tu organización en SonarCloud
- Para desarrolladores individuales, usa tu GitHub username

## Flujo de Trabajo Local

Para probar localmente:

```bash
# Backend
mvn clean verify

# Frontend
npm test -- --coverage

# Análisis local (si tienes SonarScanner instalado)
sonar-scanner \
  -Dsonar.projectKey=dsw2-practica01-backend \
  -Dsonar.sources=src/main/java \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login=YOUR_SONAR_TOKEN
```

## Recursos adicionales

- [SonarCloud Documentation](https://docs.sonarcloud.io/)
- [SonarQube Scanner for Maven](https://docs.sonarcloud.io/advanced-setup/languages/java/)
- [SonarCloud for GitHub](https://docs.sonarcloud.io/integrations/github/)
