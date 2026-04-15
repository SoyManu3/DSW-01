# Phase 0 - Research: Autenticación de empleados

## 1) Estrategia de autenticación sin endpoint de login

- Decision: Mantener autenticación exclusivamente mediante HTTP Basic Auth en cada solicitud a endpoints protegidos, sin exponer `/api/v1/auth/login` ni endpoint de validación dedicado.
- Rationale: Alinea la feature con la constitución vigente (Basic Auth por defecto), simplifica superficie de API y evita introducir mecanismos de sesión/token fuera de alcance.
- Alternatives considered:
  - Endpoint `/auth/validate`: agrega contrato extra sin beneficio funcional real.
  - Endpoint `/auth/login` con token: contradice alcance y aumenta complejidad de seguridad.

## 2) Modelo de persistencia para credenciales de empleado

- Decision: Extender la entidad persistente de `Empleado` con campos de acceso (`correo`, `passwordHash`, `intentosFallidos`, `bloqueadoHasta`, `activo`) y restricción de unicidad para `correo`.
- Rationale: El sistema ya gira en torno a `Empleado` como agregado principal; integrar credenciales evita joins innecesarios y mantiene consistencia transaccional para reglas de bloqueo y estado.
- Alternatives considered:
  - Tabla separada `empleado_credenciales`: más desacoplamiento, pero más complejidad para este alcance.
  - Autenticación en memoria (in-memory users): no satisface autenticación por correo/contraseña persistente de empleados.

## 3) Política de contraseña

- Decision: Aplicar regla mínima: 8 caracteres, al menos 1 letra y 1 número.
- Rationale: Fue clarificado por usuario, ofrece baseline de seguridad útil y fricción moderada.
- Alternatives considered:
  - Sin complejidad mínima: aumenta riesgo de contraseñas débiles.
  - Política estricta (12+ con símbolo/mayúscula): mayor seguridad, pero puede afectar adopción en MVP.

## 4) Política de bloqueo y desbloqueo

- Decision: Bloquear cuenta por 15 minutos tras 5 intentos fallidos consecutivos; desbloqueo automático con reinicio del contador.
- Rationale: Fue definido en clarificaciones y da equilibrio entre protección ante fuerza bruta y continuidad operativa.
- Alternatives considered:
  - Bloqueo manual por administrador: mayor carga operativa.
  - Bloqueo permanente: sobredimensionado para alcance actual.
  - Conservar contador tras desbloqueo: menos tolerancia a errores normales de usuario.

## 5) Integración con Spring Security

- Decision: Implementar `UserDetailsService` respaldado por base de datos usando correo como username y `passwordHash` BCrypt; mantener `SecurityFilterChain` stateless con `httpBasic`.
- Rationale: Encaja con Spring Boot 3 + Security existente, respeta constitución y aprovecha componentes probados del framework.
- Alternatives considered:
  - Proveedor de autenticación custom completo: más código y mayor riesgo de errores.
  - Migrar a OAuth2/JWT: fuera de alcance constitucional y funcional de la feature.

## 6) Documentación de contrato API

- Decision: Actualizar contrato OpenAPI del módulo de empleados para incluir campos de credenciales en alta/actualización, respuestas de error de autenticación/bloqueo y esquema `basicAuth`.
- Rationale: Mantiene trazabilidad entre implementación y contrato público; cumple requisito constitucional de OpenAPI + Swagger.
- Alternatives considered:
  - Mantener contrato previo y documentar informalmente: incumple gobernanza de interfaces.

## 7) Estrategia de pruebas

- Decision: Añadir pruebas unitarias para validación de contraseña, contador de intentos y lógica de bloqueo/desbloqueo; añadir pruebas de integración para Basic Auth con correo/contraseña, denegación por bloqueo y acceso CRUD autenticado.
- Rationale: Las reglas nuevas son seguridad-críticas y requieren cobertura ejecutable en unidad e integración.
- Alternatives considered:
  - Solo unitarias: no valida cadena real de seguridad HTTP.
  - Solo integración: ciclo más lento para validar reglas de negocio finas.

## Resolución de NEEDS CLARIFICATION

- No quedan `NEEDS CLARIFICATION` en contexto técnico ni en decisiones de diseño.
- Políticas clave cerradas por clarificación de usuario:
  - Contraseña: mínimo 8, al menos letra + número.
  - Bloqueo: 5 fallos consecutivos -> 15 minutos.
  - Desbloqueo: automático con reinicio de contador.
  - Autorización: cualquier empleado autenticado puede CRUD completo.
  - Flujo: sin endpoint de login dedicado; validación en endpoints protegidos.
