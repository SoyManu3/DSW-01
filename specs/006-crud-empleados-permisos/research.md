# Research: CRUD visual de empleados con permisos por rol

## Decision 1: Fuente de verdad de permisos

- **Decision**: Resolver permisos en frontend usando el rol devuelto por backend en el login (`ADMIN` o `EMPLEADO`).
- **Rationale**: Evita reglas frágiles basadas en nombre de usuario (por ejemplo `admin`) y mantiene consistencia con seguridad backend.
- **Alternatives considered**:
  - Comparar username fijo (`admin`): descartado por acoplamiento y baja escalabilidad.
  - Configuración local de admins en frontend: descartado por riesgo de desalineación y seguridad.

## Decision 2: Comportamiento de acceso no autorizado por URL

- **Decision**: Si un empleado accede a rutas de crear/editar, redirigir al listado de empleados en modo lectura y mostrar mensaje de permisos insuficientes.
- **Rationale**: UX clara, evita exposición de formularios no permitidos y coincide con clarificación aprobada.
- **Alternatives considered**:
  - Mostrar pantalla de acceso denegado: descartado por mayor fricción de navegación.
  - Mostrar formulario deshabilitado: descartado por complejidad visual y menor claridad.

## Decision 3: Estrategia de autorización en UI

- **Decision**: Aplicar doble control en frontend: guard de rutas para páginas de escritura + ocultar/deshabilitar acciones en la vista de listado.
- **Rationale**: Reduce intentos accidentales y mejora experiencia de solo lectura para rol empleado.
- **Alternatives considered**:
  - Solo ocultar botones: descartado porque no protege navegación directa por URL.
  - Solo guard de rutas: descartado porque deja señales ambiguas en listado.

## Decision 4: Contrato de interacción frontend-backend

- **Decision**: Reutilizar endpoints REST existentes de `/api/v1/empleados` y documentar contrato de permisos UI en un artefacto markdown específico de feature.
- **Rationale**: El alcance es visual con control por rol; no exige crear nuevos endpoints para CRUD base.
- **Alternatives considered**:
  - Crear endpoint nuevo de permisos dedicado: descartado por fuera de alcance actual.
  - Redefinir OpenAPI completo de empleados: descartado por no aportar valor incremental inmediato.

## Decision 5: Estrategia de pruebas

- **Decision**: Cubrir matriz de permisos con pruebas de componente/guard y E2E (admin CRUD permitido, empleado solo lectura y redirecciones por URL).
- **Rationale**: Verifica comportamiento funcional y de seguridad en puntos de mayor riesgo de regresión.
- **Alternatives considered**:
  - Solo pruebas unitarias: descartado por no validar flujos completos de autorización.
  - Solo E2E: descartado por menor capacidad de aislar fallas en reglas de guard/estado.
