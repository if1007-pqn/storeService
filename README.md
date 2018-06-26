# store service
a store microservice over spring boot and mongo

## running only local application
<pre> ./gradlew run </pre>

## running tests
<pre> ./run-tests.sh </pre>

## build and push to AWS EKS
* this script requires account in docker hub:
<pre> bash eks/push-eks.sh $LOGSERVICE_ADDR</pre>

LOGSERVICE_ADDR is the logservice address (with port), used to logstash and packetbeat in storeservice container, send the logs and metrics respectively.


## docs/api
run the project and access the swagger page:
<pre> GET /swagger-ui.html </pre>
or
[here](https://github.com/if1007/storeService/wiki/api) (may be outdated)
