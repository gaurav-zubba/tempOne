FROM maven:3.6.3-openjdk-11-slim as builder

WORKDIR /

COPY pom.xml .

COPY src ./src

RUN mvn clean
RUN mvn package


FROM adoptopenjdk/openjdk11:ubi


WORKDIR /

COPY configuration.yml /
COPY --from=builder /target/LeanIXFPE-1.0-SNAPSHOT.jar /target

RUN java -version

CMD ["java","-jar","target/LeanIXFPE-1.0-SNAPSHOT.jar","configuration.yml"]

EXPOSE 4003
