#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="docker/docker-compose.yml"

docker compose -f "$COMPOSE_FILE" up --build -d frontend

for _ in {1..30}; do
  if curl -fsS "http://localhost:4200" >/dev/null; then
    echo "Frontend smoke OK"
    exit 0
  fi
  sleep 2
done

echo "Frontend smoke failed: http://localhost:4200 not reachable" >&2
exit 1
