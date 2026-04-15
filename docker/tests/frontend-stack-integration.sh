#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="docker/docker-compose.yml"

docker compose -f "$COMPOSE_FILE" up --build -d

for _ in {1..30}; do
  if curl -fsS "http://localhost:4200" >/dev/null; then
    break
  fi
  sleep 2
done

curl -fsS "http://localhost:4200" >/dev/null
curl -fsS "http://localhost:8080/v3/api-docs" >/dev/null

echo "Frontend + API + Postgres integration OK"
