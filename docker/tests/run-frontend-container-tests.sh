#!/usr/bin/env bash
set -euo pipefail

trap 'docker compose -f docker/docker-compose.yml down' EXIT

bash docker/tests/frontend-smoke.sh
bash docker/tests/frontend-stack-integration.sh
bash docker/tests/frontend-runtime-config.sh

echo "All frontend container tests passed"
