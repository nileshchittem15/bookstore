FROM openjdk:8-jre-alpine

MAINTAINER nilesh <nileshchittemreddy@gmail.com>

RUN rm -rf /src
WORKDIR /src
COPY . /src

CMD ["java", "-jar", "-Dserver.port=80" , "target/bookstore-0.0.1-SNAPSHOT.jar"]
