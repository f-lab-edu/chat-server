FROM openjdk:21

RUN mkdir -p deploy
WORKDIR /deploy

COPY ./build/libs/chat-server-0.0.1-SNAPSHOT.jar chat-server.jar

ENTRYPOINT ["java","-jar","/deploy/chat-server.jar"]
