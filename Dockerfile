FROM openjdk:8u171-jdk-stretch

ARG ENV=prod
ARG LOGSERVICE_ADDRESS=http://a91dbce7e78bc11e8b9110a405e61255-1646121970.us-east-1.elb.amazonaws.com:9200

ENV LOGSERVICE_ADDRESS ${LOGSERVICE_ADDRESS}
ENV ENV ${ENV}
ENV PORT 8080
ENV PORT2 8081

ARG local=/var/app
RUN mkdir -p ${local}
WORKDIR ${local}

COPY . ${local}
# RUN ./gradlew 
EXPOSE 8080
EXPOSE 8081



CMD [ "bash", "./start.sh" ]