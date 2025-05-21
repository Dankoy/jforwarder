#!/bin/bash

if (($# < 2)); then
    echo ""
    echo "No arguments provided"
    echo "Use command example:"
    echo "docker-cv.sh OLD_VOLUME_NAME NEW_VOLUME_NAME"
    echo ""
    exit 1
fi

OLD_VOLUME_NAME="$1"
NEW_VOLUME_NAME="$2"

echo "== From '$OLD_VOLUME_NAME' to '$NEW_VOLUME_NAME' =="

function isVolumeExists {
    local isOldExists
    isOldExists=$(docker volume inspect "$1" 2>/dev/null | grep '"Name":')
    isOldExists=${isOldExists#*'"Name": "'}
    isOldExists=${isOldExists%'",'}
    isOldExists=${isOldExists##*( )}
    if [[ "$isOldExists" == "$1" ]]; then
        return 1
    else
        return 0
    fi
}

# check if old volume exists
if isVolumeExists "${OLD_VOLUME_NAME}" -eq 0 ;
then
    echo "Volume $OLD_VOLUME_NAME doesn't exist"
    exit 2
fi

# check if new volume exists
if isVolumeExists "${NEW_VOLUME_NAME}" -eq 0;
then
    echo "creating '$NEW_VOLUME_NAME' ..."
    docker volume create "${NEW_VOLUME_NAME}" 2>/dev/null 1>/dev/null
    isVolumeExists "${NEW_VOLUME_NAME}"
    if isVolumeExists "${NEW_VOLUME_NAME}" -eq 0; then
        echo "Cannot create new volume"
        exit 3
    else
        echo "OK"
    fi
else 
    echo "volume '$NEW_VOLUME_NAME' is already exist. Can't copy anything to it."
    exit 3
fi

# most important part, data migration
#docker run --rm --volume "${OLD_VOLUME_NAME}":/source --volume "${NEW_VOLUME_NAME}":/destination ubuntu:latest bash -c "echo 'copying volume ...'; cp -Rp /source/* /destination/"
#if ! func; then - to check for negative evaluation first
if docker run --rm --volume "${OLD_VOLUME_NAME}":/source --volume "${NEW_VOLUME_NAME}":/destination ubuntu:latest bash -c "echo 'copying volume ...'; cp -Rp /source/* /destination/";
then
    echo "Done successfuly 🎉"
else
    echo "Some error occured 😭"
fi
