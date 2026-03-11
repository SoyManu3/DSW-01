# Phase 0 - Research: CRUD de empleados

## 1) Validación de longitud (100 caracteres)

- Decision: Aplicar validación en dos capas, API y base de datos. En API usar Bean Validation (`@NotBlank`, `@Size(max = 100)`), y en BD usar `VARCHAR(100)`.
- Rationale: Se evita persistir datos inválidos y se mantiene integridad incluso ante accesos no previstos por API.
- Alternatives considered:
  - Solo API: simple, pero insuficiente para integridad total.
  - Solo BD: valida tarde y empeora experiencia de error.
  - Validadores custom complejos: innecesario para esta regla.

## 2) Estrategia de clave primaria `clave`

- Decision: Internamente la PK será compuesta (`prefijo` fijo `EMP` + `numero_autonumerico`), y externamente se expondrá `clave` derivada con formato `EMP-<n>` autogenerada e inmutable.
- Rationale: Cumple la nueva definición funcional de clave, preserva trazabilidad y evita entrada manual de identificadores.
- Alternatives considered:
  - PK textual simple `clave`: no cumple requisito de PK compuesta.
  - UUID autogenerado: robusto, pero contradice formato de negocio `EMP-<n>`.

## 3) Seguridad Basic Auth

- Decision: Configurar HTTP Basic Auth para endpoints CRUD (`/api/v1/empleados/**`), con Swagger público solo en desarrollo (`/v3/api-docs/**`, `/swagger-ui/**`).
- Rationale: Respeta la constitución (Basic Auth por defecto) y permite inspección de contrato en entorno de desarrollo.
- Alternatives considered:
  - Todo protegido (incluyendo Swagger): más estricto, menor usabilidad durante desarrollo.
  - JWT/OAuth2: sobredimensionado para alcance actual.

## 4) PostgreSQL con Docker

- Decision: Proveer base local con Docker Compose y variables de entorno (`SPRING_DATASOURCE_*`).
- Rationale: Entorno reproducible y onboarding rápido.
- Alternatives considered:
  - PostgreSQL instalado localmente: menos portable.
  - H2 en desarrollo: divergencia frente a PostgreSQL real.

## 5) Estrategia de pruebas

- Decision: Pruebas unitarias para servicio/validación y pruebas de integración con Spring Boot Test + Testcontainers PostgreSQL para flujos CRUD y seguridad.
- Rationale: Equilibrio entre velocidad y confiabilidad de comportamiento real.
- Alternatives considered:
  - Solo unitarias: no cubren integración real.
  - Solo integración: más lentas y costosas para ciclo diario.

## 6) Swagger/OpenAPI

- Decision: Usar `springdoc-openapi-starter-webmvc-ui` y exponer `/v3/api-docs` y `/swagger-ui/index.html` en desarrollo.
- Rationale: Integración directa y compatible con Spring Boot 3.
- Alternatives considered:
  - Springfox: no recomendado para Boot 3.
  - OpenAPI manual sin springdoc: mayor mantenimiento.

## Resolución de NEEDS CLARIFICATION

- No quedan `NEEDS CLARIFICATION` en el contexto técnico del plan.
- Política adoptada para seguridad en desarrollo: Swagger público y CRUD protegido con Basic Auth.
