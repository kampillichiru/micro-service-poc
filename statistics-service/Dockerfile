FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/statistics-service.jar statistics-service.jar
CMD ["java", "-Xmx200m", "-jar", "statistics-service.jar"]

EXPOSE 7000