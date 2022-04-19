FROM openjdk:8-jdk-alpine
RUN mkdir -p /opt/gateways
RUN chmod -R 777 /opt/gateways
# Needed to fix 'Fontconfig warning: ignoring C.UTF-8: not a valid language tag'
ENV LANG en_GB.UTF-8
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*

ARG JAR_FILE
ADD ${JAR_FILE} /opt/gateways/app.jar
COPY src/main/resources/application.properties /opt/gateways/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/gateways/app.jar", "--spring.config.location=file:/opt/gateways/application.properties"]
