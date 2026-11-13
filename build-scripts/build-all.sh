
#!/usr/bin/env bash

# ==========================================================
# EasyBank Microservices - Docker Image Builder
# ==========================================================

set -Eeuo pipefail

# ----------------------------------------------------------
# Configuration
# ----------------------------------------------------------

DOCKER_USERNAME="marcell13"
TAG="${1:-s6}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_DIR="$SCRIPT_DIR/logs"

TIMESTAMP="$(date '+%Y%m%d_%H%M%S')"
LOG_FILE="$LOG_DIR/build_${TIMESTAMP}.log"

# Actual microservice directory names
SERVICES=(
    "accounts"
    "cards"
    "loans"
    "configServer"
    "eurekaserver"
)

SUCCESSFUL=()
FAILED=()

# ----------------------------------------------------------
# Prepare logging
# ----------------------------------------------------------

mkdir -p "$LOG_DIR"

exec > >(tee -a "$LOG_FILE") 2>&1

# ----------------------------------------------------------
# Header
# ----------------------------------------------------------

echo "=========================================================="
echo "             EASYBANK DOCKER IMAGE BUILDER"
echo "=========================================================="
echo "Started          : $(date)"
echo "Project directory: $PROJECT_DIR"
echo "Docker username  : $DOCKER_USERNAME"
echo "Image tag        : $TAG"
echo "Log file         : $LOG_FILE"
echo "=========================================================="

# ----------------------------------------------------------
# Verify Docker
# ----------------------------------------------------------

if ! command -v docker >/dev/null 2>&1; then
    echo "ERROR: Docker is not installed."
    exit 1
fi

if ! docker info >/dev/null 2>&1; then
    echo "ERROR: Docker Engine is not running or inaccessible."
    exit 1
fi

# ----------------------------------------------------------
# Build microservices
# ----------------------------------------------------------

for SERVICE in "${SERVICES[@]}"; do

    SERVICE_DIR="$PROJECT_DIR/$SERVICE"
    MVN="$SERVICE_DIR/mvnw"
    POM_FILE="$SERVICE_DIR/pom.xml"

    # Docker image names must be lowercase
    IMAGE_NAME="$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')"

    IMAGE="$DOCKER_USERNAME/$IMAGE_NAME:$TAG"

    echo ""
    echo "=========================================================="
    echo "BUILDING: $SERVICE"
    echo "Docker image: $IMAGE"
    echo "Started: $(date)"
    echo "=========================================================="

    # Check service directory
    if [[ ! -d "$SERVICE_DIR" ]]; then
        echo "ERROR: Service directory not found: $SERVICE_DIR"
        FAILED+=("$SERVICE")
        break
    fi

    # Check Maven Wrapper
    if [[ ! -f "$MVN" ]]; then
        echo "ERROR: Maven Wrapper not found: $MVN"
        FAILED+=("$SERVICE")
        break
    fi

    # Check POM
    if [[ ! -f "$POM_FILE" ]]; then
        echo "ERROR: POM not found: $POM_FILE"
        FAILED+=("$SERVICE")
        break
    fi

    chmod +x "$MVN"

    # Build Docker image
    if (
        cd "$SERVICE_DIR"

        ./mvnw -B \
            -Ddocker.image.name="$IMAGE" \
            compile jib:dockerBuild
    ); then

        echo ""
        echo "SUCCESS: $SERVICE"
        echo "Image created: $IMAGE"

        SUCCESSFUL+=("$SERVICE")

    else

        echo ""
        echo "FAILED: $SERVICE"

        FAILED+=("$SERVICE")
        break

    fi

done

# ----------------------------------------------------------
# Build summary
# ----------------------------------------------------------

echo ""
echo "=========================================================="
echo "                    BUILD SUMMARY"
echo "=========================================================="

echo "Completed: $(date)"
echo "Total services : ${#SERVICES[@]}"
echo "Successful     : ${#SUCCESSFUL[@]}"
echo "Failed         : ${#FAILED[@]}"

if ((${#SUCCESSFUL[@]} > 0)); then
    echo ""
    echo "SUCCESSFUL BUILDS:"

    for SERVICE in "${SUCCESSFUL[@]}"; do
        IMAGE_NAME="$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')"
        echo "  [SUCCESS] $DOCKER_USERNAME/$IMAGE_NAME:$TAG"
    done
fi

if ((${#FAILED[@]} > 0)); then
    echo ""
    echo "FAILED BUILDS:"

    for SERVICE in "${FAILED[@]}"; do
        echo "  [FAILED] $SERVICE"
    done
fi

echo ""
echo "Build log: $LOG_FILE"
echo "=========================================================="

if ((${#FAILED[@]} > 0)); then
    echo "BUILD PROCESS FINISHED WITH ERRORS."
    exit 1
fi

echo "ALL DOCKER IMAGES BUILT SUCCESSFULLY!"

exit 0
