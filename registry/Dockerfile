FROM openjdk:8-jdk-alpine
#FROM java:8-jre
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/registry.jar registry.jar
CMD ["java", "-Xmx200m", "-jar", "registry.jar"]

EXPOSE 8761
