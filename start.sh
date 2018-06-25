#!/usr/bin/env bash


if [ $ENV = 'prod' ]; then
    ./run-log.sh &
fi

if [ $ENV = 'test' ]; then 
    ./gradlew test
else
    ./gradlew run
fi
