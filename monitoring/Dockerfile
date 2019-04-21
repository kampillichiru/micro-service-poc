FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/monitoring.jar monitoring.jar
CMD ["java", "-Xmx200m", "-jar", "monitoring.jar"]

EXPOSE 8989 8080