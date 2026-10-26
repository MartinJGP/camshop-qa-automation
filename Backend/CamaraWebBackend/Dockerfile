FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S camshop && adduser -S camshop -G camshop \
    && mkdir -p /app/uploads && chown -R camshop:camshop /app
COPY --from=build --chown=camshop:camshop /app/target/*.jar app.jar
USER camshop
EXPOSE 8090
VOLUME ["/app/uploads"]
ENV UPLOAD_DIR=/app/uploads
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
