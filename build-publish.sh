#!/bin/bash

if [[ "$NAWAMAN_SIGNING_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SIGNING_PASSWORD is not set."
    exit -1
fi

if [[ "$NAWAMAN_SONATYPE_PASSWORD" == "" ]]; then
    echo "NAWAMAN_SONATYPE_PASSWORD is not set."
    exit -1
fi

if [[ "$NULLABLEJ_KEYNAME" == "" ]]; then
    echo "NULLABLEJ_KEYNAME is not set."
    exit -1
fi

./mvnw clean install package deploy
