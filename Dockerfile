FROM openjdk:11.0.14-jdk-oracle
COPY app/build/libs/app-0.0.1-SNAPSHOT.jar wallet
ENTRYPOINT ["java","-jar","/wallet"]
EXPOSE 8888