# Quickstart - Paginación de empleados (10 por página)

## Prerrequisitos

- API de empleados en ejecución
- Basic Auth válido

## 1) Consultar primera página

```bash
curl -u admin:admin123 "http://localhost:8080/api/v1/empleados?page=0"
```

Respuesta esperada:

- HTTP `200`
- JSON con campos `content`, `page`, `size`, `totalElements`, `totalPages`
- `size = 10`

## 2) Consultar segunda página

```bash
curl -u admin:admin123 "http://localhost:8080/api/v1/empleados?page=1"
```

Respuesta esperada:

- HTTP `200`
- `page = 1`
- `content` con hasta 10 elementos

## 3) Verificar comportamiento de page negativa

```bash
curl -u admin:admin123 "http://localhost:8080/api/v1/empleados?page=-1"
```

Respuesta esperada:

- HTTP `200`
- Paginación normalizada a primera página (`page = 0`)

## 4) Confirmar seguridad

- Sin credenciales válidas, la API responde `401`.
