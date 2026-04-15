#!/bin/sh
set -eu

RUNTIME_CONFIG_TEMPLATE="/usr/share/nginx/html/runtime-config.template.js"
RUNTIME_CONFIG_OUTPUT="/usr/share/nginx/html/runtime-config.js"
API_BASE_URL="${API_BASE_URL:-http://localhost:8080/api/v1}"

sed "s|\${API_BASE_URL}|${API_BASE_URL}|g" "${RUNTIME_CONFIG_TEMPLATE}" > "${RUNTIME_CONFIG_OUTPUT}"
