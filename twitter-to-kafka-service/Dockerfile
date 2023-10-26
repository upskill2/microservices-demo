FROM openjdk:11-jre-slim
#EXPOSE 8080
ADD /target/twitter-to-kafka-service-0.0.1-SNAPSHOT.jar twitter-to-kafka-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","twitter-to-kafka-service-0.0.1-SNAPSHOT.jar"]