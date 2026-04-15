# Checklist de medición SC-004: Reconocimiento de capacidades por rol

## Objetivo

Validar que al menos 19 de 20 ejecuciones identifican correctamente capacidades por rol en menos de 10 segundos.

## Protocolo

- 10 ejecuciones con sesión `ADMIN`.
- 10 ejecuciones con sesión `EMPLEADO`.
- Medición desde render inicial del módulo empleados hasta confirmación verbal/registrada del alcance de permisos.

## Registro

| Run | Rol | Tiempo (s) | Identificación correcta (Sí/No) |
| --- | --- | ---------: | ------------------------------- |
| 1   |     |            |                                 |
| 2   |     |            |                                 |
| 3   |     |            |                                 |
| 4   |     |            |                                 |
| 5   |     |            |                                 |
| 6   |     |            |                                 |
| 7   |     |            |                                 |
| 8   |     |            |                                 |
| 9   |     |            |                                 |
| 10  |     |            |                                 |
| 11  |     |            |                                 |
| 12  |     |            |                                 |
| 13  |     |            |                                 |
| 14  |     |            |                                 |
| 15  |     |            |                                 |
| 16  |     |            |                                 |
| 17  |     |            |                                 |
| 18  |     |            |                                 |
| 19  |     |            |                                 |
| 20  |     |            |                                 |

## Criterio de aceptación

- Se cumple SC-004 si:
  - `correctas >= 19`, y
  - `tiempo <= 10s` en al menos 19 ejecuciones.
