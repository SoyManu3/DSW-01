# Phase 0 - Research: CRUD de Departamentos y Relación con Empleados

## 1) Modelado de relación Departamento-Empleado (1:N obligatorio)

- Decision: Crear entidad `Departamento` y agregar referencia obligatoria de `Empleado` hacia `Departamento` mediante clave foránea no nula.
- Rationale: Cumple la regla clarificada de que todo empleado debe pertenecer a un departamento y permite consultas directas por departamento.
- Alternatives considered:
  - Relación opcional (`departamento_id` nullable): rechazada porque contradice clarificación funcional.
  - Tabla intermedia N:M: rechazada porque el dominio exige 1:N simple.

## 2) Estrategia de borrado de departamentos con integridad referencial

- Decision: Rechazar eliminación de un departamento con empleados asociados con error de negocio explícito.
- Rationale: Evita pérdida de trazabilidad y evita efectos colaterales no deseados en empleados existentes.
- Alternatives considered:
  - Borrado en cascada de empleados: rechazada por riesgo funcional y pérdida de información.
  - Reasignación automática a “Sin departamento”: rechazada porque se definió departamento obligatorio y no existe esa entidad en alcance.

## 3) Regla de unicidad para nombre de departamento

- Decision: Aplicar unicidad sensible a mayúsculas/minúsculas para `nombre`.
- Rationale: Fue una aclaración explícita del usuario y mantiene una regla simple, verificable y consistente con el dominio solicitado.
- Alternatives considered:
  - Unicidad case-insensitive: rechazada por decisión de clarificación.
  - Sin unicidad: rechazada por ambigüedad operativa.

## 4) Validación de campos de departamento

- Decision: Validar `nombre` como obligatorio, no en blanco y con longitud máxima de 100 caracteres.
- Rationale: Regla clara para calidad de datos y coherencia con límites usados en otras entidades del proyecto.
- Alternatives considered:
  - Máximo 255: viable pero más permisivo de lo requerido.
  - Sin límite: rechazada por riesgo de datos inconsistentes y payloads excesivos.

## 5) Diseño de endpoints para empleados por departamento

- Decision: `GET /api/v1/departamentos/{id}` devuelve datos del departamento + contador de empleados; `GET /api/v1/departamentos/{id}/empleados` devuelve listado paginado.
- Rationale: Mantiene el endpoint de detalle liviano y separa la consulta de colección para paginación determinista.
- Alternatives considered:
  - Embeber todos los empleados en detalle: rechazada por posible crecimiento no acotado de respuesta.
  - Omitir consulta por departamento: rechazada por incumplir requerimiento funcional.

## 6) Paginación y orden determinista

- Decision: Fijar tamaño de página en 10 elementos y orden por defecto `id` ascendente para listados de departamentos y empleados por departamento.
- Rationale: Cumple constitución (paginación de 10) y evita inestabilidad entre páginas.
- Alternatives considered:
  - Orden no definido: rechazada por resultados no deterministas.
  - Orden por `nombre`: rechazada por no ser la preferencia aclarada.

## 7) Migraciones y compatibilidad de datos existentes

- Decision: Añadir migración Flyway para crear `departamentos`, poblar un departamento por defecto para datos existentes de `empleados`, y agregar FK no nula hacia `departamentos`.
- Rationale: Permite evolucionar esquema sin romper registros actuales y mantiene despliegues repetibles.
- Alternatives considered:
  - Exigir limpieza manual previa de datos: rechazada por alto riesgo operativo.
  - Dejar FK nullable temporalmente: rechazada por contradecir regla funcional final.

## 8) Pruebas automatizadas

- Decision: Implementar pruebas unitarias para validaciones/reglas de borrado y pruebas de integración para seguridad, endpoints nuevos, paginación y reglas de integridad.
- Rationale: Los cambios tocan contrato HTTP, seguridad y persistencia; requieren verificación ejecutable completa.
- Alternatives considered:
  - Solo unitarias: rechazada por no validar capa HTTP + seguridad.
  - Solo integración: rechazada por feedback más lento para reglas de negocio puntuales.

## Resolución de NEEDS CLARIFICATION

No quedan `NEEDS CLARIFICATION` en el contexto técnico ni en decisiones de diseño para esta feature.
