FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/auth-service.jar auth-service.jar
CMD ["java", "-Xmx200m", "-jar", "auth-service.jar"]

EXPOSE 5000