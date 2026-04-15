# Evidencia SC-003: Identificación de permisos por rol

## Objetivo

Validar que al menos 95% de participantes identifica correctamente sus permisos en menos de 10 segundos desde la carga del módulo de departamentos.

## Protocolo

- Muestra objetivo: 20 participantes representativos.
  - 10 con rol `ADMIN`
  - 10 con rol `EMPLEADO`
- Métrica por ejecución:
  - `tiempo_segundos` desde carga de `/departamentos` hasta respuesta del participante.
  - `acierto` (`SI`/`NO`) sobre identificación correcta de permisos.
- Instrumentación:
  - Cronómetro manual o digital.
  - Registro inmediato en tabla de resultados.

## Criterio de éxito

- Éxito global si:
  - Aciertos >= 19 de 20 (95%), y
  - Cada acierto dentro de `<= 10` segundos.

## Tabla de ejecución

| # | Rol | Tiempo (s) | Acierto (SI/NO) | Observaciones |
|---|-----|------------|-----------------|---------------|
| 1 | ADMIN | | | |
| 2 | ADMIN | | | |
| 3 | ADMIN | | | |
| 4 | ADMIN | | | |
| 5 | ADMIN | | | |
| 6 | ADMIN | | | |
| 7 | ADMIN | | | |
| 8 | ADMIN | | | |
| 9 | ADMIN | | | |
| 10 | ADMIN | | | |
| 11 | EMPLEADO | | | |
| 12 | EMPLEADO | | | |
| 13 | EMPLEADO | | | |
| 14 | EMPLEADO | | | |
| 15 | EMPLEADO | | | |
| 16 | EMPLEADO | | | |
| 17 | EMPLEADO | | | |
| 18 | EMPLEADO | | | |
| 19 | EMPLEADO | | | |
| 20 | EMPLEADO | | | |

## Resultado

- Aciertos totales: ____ / 20
- Dentro de 10s: ____ / 20
- Estado SC-003: ____ (PASS/FAIL)
- Fecha de ejecución: ____
- Responsable: ____
