FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/zipkin-server.jar zipkin-server.jar
CMD ["java", "-Xmx200m", "-jar", "zipkin-server.jar"]

EXPOSE 9411