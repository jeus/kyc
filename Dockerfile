FROM registry.becopay.com/devops/java-docker
MAINTAINER jeus
ADD target/*.jar kyc.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/kyc.jar"]
EXPOSE 9092