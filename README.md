# store service
a store microservice over spring boot and mongo

## running only local application (dev)
<pre> ./gradlew run </pre>

## running container local (prod)
* used to test communication with logservice local, firstly run the run.sh of logservice, then do it:
<pre> ./run.sh </pre>

## running tests (test)
<pre> ENV=test ./run.sh </pre>


## build and push to AWS EKS
* this script requires account in docker hub:
<pre> bash eks/push-eks.sh $LOGSERVICE_ADDR</pre>

LOGSERVICE_ADDR is the logservice address (with port), used to logstash and packetbeat in storeservice container, send the logs and metrics respectively.


## docs/api
run the project and access the swagger page:
<pre> GET /swagger-ui.html </pre>
or
[here](https://github.com/if1007/storeService/wiki/api) (may be outdated)
