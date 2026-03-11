<!--
Sync Impact Report
- Version change: 1.0.0 → 1.1.0
- Modified principles:
  - IV. Contrato de API y Documentación Swagger Siempre Disponible → IV. Contrato OpenAPI, Swagger y Estabilidad de Interfaces
- Added sections:
  - VI. Versionado Obligatorio de API y Paginación Determinista
- Removed sections:
  - Ninguna
- Templates requiring updates:
  - ✅ updated: .specify/templates/plan-template.md
  - ✅ updated: .specify/templates/spec-template.md
  - ✅ updated: .specify/templates/tasks-template.md
  - ⚠ pending: .specify/templates/commands/*.md (directorio no existe en el repositorio)
- Deferred TODOs:
  - Ninguno
-->

# DSW2 Backend Constitution

## Core Principles

### I. Spring Boot 3 + Java 17 Obligatorio

Todo servicio backend MUST implementarse con Spring Boot 3 y Java 17. No se permite
introducir frameworks alternativos para el núcleo HTTP ni versiones de Java distintas en
producción. Rationale: homogeneidad tecnológica, soporte LTS y reducción de complejidad
operativa.

### II. Seguridad con HTTP Basic Auth por Defecto

Los endpoints protegidos MUST exigir autenticación HTTP Basic Auth y las reglas de acceso
MUST declararse explícitamente en la configuración de seguridad. Las excepciones públicas
MUST estar justificadas en la especificación de la funcionalidad. Rationale: baseline de
seguridad simple, verificable y consistente para un proyecto backend inicial.

### III. Persistencia PostgreSQL con Docker para Entornos Locales

La persistencia de datos MUST usar PostgreSQL. El aprovisionamiento en desarrollo/local y
pruebas de integración MUST poder ejecutarse con Docker (preferiblemente Docker Compose),
incluyendo configuración reproducible de variables de entorno. Rationale: paridad de
entornos y arranque rápido del equipo.

### IV. Contrato OpenAPI, Swagger y Estabilidad de Interfaces

Toda API REST MUST exponer especificación OpenAPI y una interfaz Swagger accesible en
entornos de desarrollo. Cada cambio de endpoint MUST reflejarse en el contrato publicado.
Rationale: trazabilidad funcional, integración más segura y menor ambigüedad para clientes.

### V. Calidad Ejecutable: Pruebas y Criterios de Aceptación

Cada cambio funcional MUST incluir pruebas automatizadas proporcionales (unitarias y/o de
integración) y criterios de aceptación verificables. Todo cambio de seguridad, acceso a
datos o contrato HTTP MUST tener cobertura de integración. Rationale: prevenir regresiones
en áreas de alto impacto.

### VI. Versionado Obligatorio de API y Paginación Determinista

Todo endpoint HTTP público MUST incluir versión explícita en su ruta base
(ejemplo: `/api/v1/...`). Las operaciones de consulta de colecciones MUST devolver
resultados paginados con tamaño fijo de 10 instancias por consulta. Rationale: permitir
evolución controlada del contrato sin romper clientes y garantizar cargas de respuesta
predecibles.

## Restricciones Técnicas y de Seguridad

- Build y ejecución MUST gestionarse con Maven o Gradle manteniendo compatibilidad con Java 17.
- La configuración sensible (credenciales, URLs, secretos) MUST provenir de variables de
  entorno o mecanismos externos equivalentes; no se permite hardcode en código fuente.
- Los cambios de esquema de base de datos MUST gestionarse con migraciones versionadas para
  asegurar despliegues repetibles.
- Los servicios MUST proveer manejo de errores consistente (códigos HTTP, payload de error y
  logging estructurado mínimo).

## Flujo de Desarrollo y Puertas de Calidad

- Toda especificación de feature MUST declarar impacto en seguridad, contrato API y modelo de
  datos antes de implementación.
- Toda especificación de feature MUST declarar versión de API en rutas y comportamiento de
  paginación para endpoints de consulta de colecciones.
- Todo plan MUST pasar Constitution Check antes de iniciar diseño detallado.
- Toda tarea de implementación MUST mapearse a historias de usuario independientes y verificables.
- Ningún cambio se considera listo si falla compilación, pruebas obligatorias o validación de
  documentación OpenAPI/Swagger.

## Governance

Esta constitución prevalece sobre guías de implementación de menor jerarquía. Toda PR MUST
demostrar cumplimiento constitucional en revisión.

Proceso de enmienda:

- Cualquier enmienda MUST incluir motivo, alcance, impacto en plantillas y plan de migración.
- La aprobación requiere acuerdo explícito de mantenedores del proyecto.
- La enmienda entra en vigor al actualizar este documento y su Sync Impact Report.

Política de versionado constitucional (SemVer):

- MAJOR: eliminación o redefinición incompatible de principios/gobernanza.
- MINOR: adición de principio/sección o expansión normativa material.
- PATCH: aclaraciones editoriales sin cambio normativo.

Revisión de cumplimiento:

- En planificación: validar Constitution Check en plan-template.
- En especificación: incluir requisitos de alineación constitucional.
- En ejecución: verificar tareas de seguridad, base de datos, Docker, Swagger, versionado
  en rutas y paginación de consultas.

**Version**: 1.1.0 | **Ratified**: 2026-02-25 | **Last Amended**: 2026-03-04
