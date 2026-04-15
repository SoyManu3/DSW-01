# Phase 1 - Data Model: Autenticación de empleados

## Entity: Empleado

Representa al empleado de negocio y su cuenta de acceso al sistema.

### Campos de identidad y negocio

- `prefijo` (String, PK compuesta)
  - Reglas:
    - Valor fijo `EMP`
    - Obligatorio, inmutable
- `numero_autonumerico` (Long, PK compuesta)
  - Reglas:
    - Entero positivo autoincremental
    - Obligatorio, inmutable
- `clave` (String, derivado)
  - Reglas:
    - Formato `EMP-<numero_autonumerico>`
    - Expuesto en API, no editable
- `nombre` (String)
  - Reglas:
    - Obligatorio
    - Longitud 1..100
- `direccion` (String)
  - Reglas:
    - Obligatorio
    - Longitud 1..100
- `telefono` (String)
  - Reglas:
    - Obligatorio
    - Longitud 1..100

### Campos de acceso

- `correo` (String)
  - Reglas:
    - Obligatorio
    - Formato email válido
    - Único global en empleados
    - Usado como username de autenticación Basic Auth
- `passwordHash` (String)
  - Reglas:
    - Obligatorio
    - Almacena hash BCrypt (no reversible)
    - Nunca se expone en respuestas API
- `activo` (Boolean)
  - Reglas:
    - Obligatorio
    - `true` permite autenticación, `false` la bloquea
- `intentosFallidos` (Integer)
  - Reglas:
    - Obligatorio
    - Incrementa por fallo de credenciales
    - Se reinicia en autenticación exitosa o al desbloquearse
- `bloqueadoHasta` (Instant/Timestamp nullable)
  - Reglas:
    - `null` = no bloqueado
    - Si `now < bloqueadoHasta`, la cuenta está bloqueada
    - Se define a `now + 15 minutos` al llegar a 5 fallos consecutivos

## Entity: EventoAutenticacion

Registra trazabilidad de accesos y rechazos de autenticación.

### Campos

- `id` (UUID/Long)
  - Reglas:
    - Identificador único del evento
- `correo` (String)
  - Reglas:
    - Correo enviado en intento (aunque no exista cuenta)
- `empleadoClave` (String nullable)
  - Reglas:
    - Se informa cuando puede asociarse a empleado existente
- `resultado` (Enum: `SUCCESS`, `INVALID_CREDENTIALS`, `BLOCKED`, `INACTIVE`)
  - Reglas:
    - Obligatorio
- `timestamp` (Instant)
  - Reglas:
    - Obligatorio
- `origen` (String nullable)
  - Reglas:
    - Contexto opcional (IP o agente) para auditoría básica

## Relaciones

- `Empleado 1 --- N EventoAutenticacion` (lógica; un empleado puede tener múltiples eventos).
- Un `EventoAutenticacion` también puede existir sin relación directa a empleado (correo inexistente).

## Value Objects / DTOs relevantes

- `EmpleadoCreateRequest`
  - `nombre`, `direccion`, `telefono`, `correo`, `password`
- `EmpleadoUpdateRequest`
  - `nombre`, `direccion`, `telefono`, `correo`, `password` (opcional para rotación)
- `EmpleadoResponse`
  - `clave`, `nombre`, `direccion`, `telefono`, `correo`, `activo` (sin `passwordHash`)

## Reglas de validación

- `correo` debe cumplir formato email y ser único.
- `password` de entrada debe cumplir: mínimo 8 caracteres, al menos 1 letra y 1 número.
- `passwordHash` solo se genera desde `password` válida.
- Tras 5 fallos consecutivos: `bloqueadoHasta = now + 15m`, `intentosFallidos` se conserva hasta desbloqueo.
- Al desbloquearse (por tiempo cumplido): `bloqueadoHasta = null`, `intentosFallidos = 0`.
- Al autenticarse correctamente: `intentosFallidos = 0`.
- Si `activo = false`, autenticación siempre rechazada.

## Estado y transiciones de autenticación (por empleado)

- `ACTIVE`: cuenta activa, no bloqueada.
- `BLOCKED_TEMPORARY`: cuenta bloqueada hasta `bloqueadoHasta`.
- `INACTIVE`: cuenta deshabilitada por negocio.

Transiciones válidas:

- `ACTIVE --(5 fallos consecutivos)--> BLOCKED_TEMPORARY`
- `BLOCKED_TEMPORARY --(tiempo expirado)--> ACTIVE` (reinicia contador)
- `ACTIVE --(desactivación)--> INACTIVE`
- `INACTIVE --(activación)--> ACTIVE`
- `ACTIVE --(auth exitosa)--> ACTIVE` (contador=0)

Transiciones inválidas:

- `BLOCKED_TEMPORARY --(auth durante bloqueo)--> ACTIVE`
- `INACTIVE --(auth válida)--> ACTIVE` sin activación explícita
