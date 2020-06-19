FROM maven:3-alpine as builder

RUN mkdir /build
WORKDIR /build

ADD pom.xml pom.xml
ADD src src

RUN mvn clean package

FROM azul/zulu-openjdk-alpine:8

RUN mkdir /app

COPY --from=builder /build/target/frontend-thorntail.jar /app/frontend-thorntail.jar

CMD [ "java", "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv4Addresses=true", "-jar", "/app/frontend-thorntail.jar" ]