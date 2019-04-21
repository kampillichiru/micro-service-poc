FROM openjdk:8-jdk-alpine

#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/notification-service.jar notification-service.jar
CMD ["java", "-Xmx200m", "-jar", "notification-service.jar"]

EXPOSE 8000