FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/gateway.jar gateway.jar
CMD ["java", "-Xmx200m", "-jar", "gateway.jar"]

EXPOSE 4000