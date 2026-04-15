# Research: CRUD visual de departamentos con permisos por rol

## Decision 1: Fuente de verdad de permisos de interfaz

- **Decision**: Resolver permisos de UI exclusivamente desde el rol retornado por backend al autenticar (`ADMIN` o `EMPLEADO`) y persistido por `AuthService`.
- **Rationale**: Evita lógica frágil basada en usernames o flags manuales y mantiene coherencia con la política de seguridad del backend.
- **Alternatives considered**:
  - Derivar permisos por nombre de usuario (`admin`): descartado por acoplamiento y baja mantenibilidad.
  - Configuración local de roles en frontend: descartado por riesgo de desalineación con backend.

## Decision 2: Presentación de acciones restringidas para rol empleado

- **Decision**: Mostrar acciones CRUD (`crear`, `editar`, `eliminar`) en estado deshabilitado cuando el usuario autenticado sea `EMPLEADO`.
- **Rationale**: Hace explícitas las capacidades disponibles y cumple la clarificación aprobada durante `/speckit.clarify`.
- **Alternatives considered**:
  - Ocultar acciones completamente: descartado por menor transparencia de capacidades.
  - Híbrido ocultar/deshabilitar según pantalla: descartado por inconsistencia y mayor costo de pruebas.

## Decision 3: Navegación ante acceso no autorizado por URL

- **Decision**: Si un empleado intenta entrar a rutas de escritura (`/departamentos/nuevo`, `/departamentos/:id/editar`), redirigir a `/departamentos` en modo lectura y mostrar `No tienes permisos para esta acción.`.
- **Rationale**: Comportamiento consistente con módulo de empleados y menor fricción que una vista de error dedicada.
- **Alternatives considered**:
  - Pantalla 403 dedicada: descartado por navegación más rígida.
  - Mantener formulario bloqueado en ruta restringida: descartado por complejidad visual sin valor adicional.

## Decision 4: Manejo de sesión inválida o expirada

- **Decision**: Para acceso a rutas protegidas sin sesión válida, redirigir a `/login` con mensaje `Sesión expirada o no autenticada.`.
- **Rationale**: Unifica comportamiento de seguridad y simplifica recuperación de sesión del usuario.
- **Alternatives considered**:
  - Mantener usuario en ruta actual con error inline: descartado por experiencia inconsistente.
  - Permitir acceso público de lectura: descartado por violar requisito de autenticación del módulo.

## Decision 5: Contrato API para frontend de departamentos

- **Decision**: Reutilizar endpoints existentes de `/api/v1/departamentos` y documentar contrato de consumo para permisos UI en dos artefactos: OpenAPI de referencia de endpoints + contrato de interacción UI.
- **Rationale**: El alcance es visual/autorización; no requiere nuevos endpoints backend.
- **Alternatives considered**:
  - Crear endpoint dedicado de permisos: descartado por fuera de alcance.
  - Reescribir OpenAPI global: descartado por costo sin beneficio incremental.

## Decision 6: Estrategia de pruebas por matriz de permisos

- **Decision**: Cubrir con tests de guardas/componentes frontend + E2E de flujos admin/empleado y redirecciones por URL.
- **Rationale**: Minimiza riesgo de escalamiento de privilegios y detecta regresiones funcionales en navegación.
- **Alternatives considered**:
  - Solo pruebas unitarias: descartado por no cubrir flujos end-to-end.
  - Solo E2E: descartado por baja capacidad de diagnóstico en fallas de reglas internas.

## Decision 7: Ajuste mínimo backend para coherencia de autorización

- **Decision**: Aplicar autorización por método HTTP en `/api/v1/departamentos/**` (`GET` autenticado, `POST/PUT/DELETE` solo `ADMIN`).
- **Rationale**: Alinea el comportamiento real de API con el contrato UI de permisos y evita escalamiento por invocación directa.
- **Alternatives considered**:
  - Mantener todos los métodos autenticados: descartado por inconsistencia con las reglas de rol de la feature.
  - Mover toda la restricción al frontend: descartado por riesgo de seguridad en backend.

## Decision 8: Persistencia de permisos tras recarga

- **Decision**: Mantener resolución de permisos desde rol persistido por `AuthService` en `localStorage` para reconstruir estado al recargar.
- **Rationale**: Cumple FR-009 sin requerir endpoint adicional de sesión.
- **Alternatives considered**:
  - Reconsultar perfil en cada recarga: descartado por complejidad adicional fuera de alcance.
  - No persistir rol localmente: descartado por pérdida de consistencia de UI tras refresh.
