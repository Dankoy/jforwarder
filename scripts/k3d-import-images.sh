#!/bin/bash

# Script for importing Docker images into k3d

set -euo pipefail

# Predefined list of images (without tags)
IMAGES=(
    "docker.io/evawe/coub_forwarder_telegram_bot"
    "docker.io/evawe/coub_smart_searcher"
    "docker.io/evawe/kafka_message_consumer"
    "docker.io/evawe/kafka_message_producer"
    "docker.io/evawe/spring_eureka_registry"
    "docker.io/evawe/spring_gateway"
    "docker.io/evawe/subscriptions_holder"
    "docker.io/evawe/subscriptions_scheduler"
    "docker.io/evawe/t_coubs_initiator"
    "docker.io/evawe/telegram_chat_service"
)

usage() {
    echo "Usage: $0 -v <version> [-i <image>] [-c <cluster>]"
    echo "  -v <version>  image version (required)"
    echo "  -i <image>    image name (optional; if omitted, all images are imported)"
    echo "  -c <cluster>  k3d cluster name (optional; defaults to k3d default cluster)"
    exit 1
}

VERSION=""
IMAGE=""
CLUSTER=""

# Leading colon enables silent mode for getopts (custom error messages)
while getopts ":v:i:c:h" opt; do
    case "$opt" in
        v) VERSION="$OPTARG" ;;
        i) IMAGE="$OPTARG" ;;
        c) CLUSTER="$OPTARG" ;;
        h) usage ;;
        \?) echo "Error: unknown option -$OPTARG" >&2; usage ;;
        :) echo "Error: option -$OPTARG requires an argument" >&2; usage ;;
    esac
done

# Validation: version is required
if [[ -z "$VERSION" ]]; then
    echo "Error: version is required (-v)" >&2
    usage
fi

if ! command -v k3d >/dev/null 2>&1; then
    echo "Error: k3d is not installed" >&2
    exit 1
fi

# Build the list of images to import
TO_IMPORT=()

if [[ -n "$IMAGE" ]]; then
    # If a specific image was provided, verify it is in the allowed list
    found=0
    for img in "${IMAGES[@]}"; do
        if [[ "$img" == "$IMAGE" || "$img" == *"/$IMAGE" ]]; then
            TO_IMPORT=("${img}:${VERSION}")
            found=1
            break
        fi
    done
    if [[ $found -eq 0 ]]; then
        echo "Error: image '$IMAGE' is not in the allowed list" >&2
        exit 1
    fi
else
    # Import all images in a single command
    for img in "${IMAGES[@]}"; do
        TO_IMPORT+=("${img}:${VERSION}")
    done
fi

# Import
echo "Importing into k3d (version: $VERSION)..."
printf '  -> %s\n' "${TO_IMPORT[@]}"

if [[ -n "$CLUSTER" ]]; then
    k3d image import "${TO_IMPORT[@]}" -c "$CLUSTER"
else
    k3d image import "${TO_IMPORT[@]}"
fi

echo "Done."