FROM openjdk:8u171-jdk-stretch

ARG ENV=prod

ENV ENV ${ENV}
ENV PORT 8080
ENV PORT2 8081

ARG local=/var/app
RUN mkdir -p ${local}
WORKDIR ${local}
RUN if [ $ENV = 'test' ]; then echo './gradlew test' >> script; else echo './gradlew run' >> script; fi

COPY . ${local}
RUN ./gradlew 
EXPOSE 8080
EXPOSE 8081



CMD [ "bash", "./script" ]