FROM maven:3.8.3-openjdk-17 as builder

RUN mkdir /build
WORKDIR /build

ADD pom.xml pom.xml
ADD src src

RUN mvn clean package

FROM openjdk:17-alpine

RUN mkdir /app

COPY --from=builder /build/target/quarkus-app /app

CMD [ "java", "-jar", "/app/quarkus-run.jar" ]