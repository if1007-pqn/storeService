#!/usr/bin/env bash


if [ $ENV = 'prod' ]; then
    ./run-log.sh $LOGSERVICE_ADDRESS &
fi

if [ $ENV = 'test' ]; then 
    ./gradlew test
else
    ./gradlew run
fi
