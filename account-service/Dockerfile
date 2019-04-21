FROM openjdk:8-jdk-alpine
#MAINTAINER Alexander Lukyanchikov <sqshq@sqshq.com>

ADD ./target/account-service.jar account-service.jar
CMD ["java", "-Xmx200m", "-jar", "account-service.jar"]

EXPOSE 6000