#!/usr/bin/env bash
local="$(cd "$(dirname "$0")"; pwd)"
cd "$local"

if [ ! "$ENV" = 'test' ]; then
    extra="-p 8080:8080 --link logservice"
    ENV="prod"
fi

function clean_mongo() {
    docker rm --force mongo 2>/dev/null >&2
}

clean_mongo
docker run --name mongo mongo &
if ! docker images | grep -w -E -q "^storeservice$ENV"; then
    docker build . -t storeservice$ENV --build-arg ENV=$ENV --build-arg LOG_ADDR='http://logservice:9200'
else
    sleep 5 #time to mongo load
fi

docker run --link mongo $extra storeservice$ENV && clean_mongo
