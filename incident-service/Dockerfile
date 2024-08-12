FROM public.ecr.aws/docker/library/maven:3-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM public.ecr.aws/amazoncorretto/amazoncorretto:21-al2023-headless AS production

WORKDIR /app

COPY --from=builder app/target/incident-service-*.jar ./incident-service.jar

EXPOSE 8080

CMD ["java", "-jar", "incident-service.jar"]
