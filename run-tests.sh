#!/usr/bin/env bash
function clean_mongo() {
    docker rm --force mongo 2>/dev/null >&2
}

clean_mongo
docker run --name mongo mongo &
if ! docker images | grep -w -q storeservicetest; then
    docker build . -t storeservicetest --build-arg ENV=test
else
    sleep 5 #time to mongo load
fi
docker run --link mongo storeservicetest && clean_mongo
