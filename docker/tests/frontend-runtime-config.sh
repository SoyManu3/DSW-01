#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="docker/docker-compose.yml"
CONTAINER_NAME="empleados-frontend"

docker compose -f "$COMPOSE_FILE" up --build -d frontend

docker exec "$CONTAINER_NAME" sh -c "grep -q 'http://localhost:8080/api/v1' /usr/share/nginx/html/runtime-config.js"

docker compose -f "$COMPOSE_FILE" down

API_BASE_URL="http://api:8080/api/v1" docker compose -f "$COMPOSE_FILE" up --build -d frontend

docker exec "$CONTAINER_NAME" sh -c "grep -q 'http://api:8080/api/v1' /usr/share/nginx/html/runtime-config.js"

echo "Runtime config default/override OK"
