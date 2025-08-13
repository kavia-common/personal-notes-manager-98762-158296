#!/usr/bin/env bash
# Proxy Gradle wrapper at repo root to delegate to the android_frontend/gradlew
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR/android_frontend"

exec ./gradlew "$@"
