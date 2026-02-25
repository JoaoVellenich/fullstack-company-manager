# -------- STAGE 1 - Build --------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia pom primeiro para aproveitar cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do projeto
COPY src ./src

# Gera o jar
RUN mvn clean package -DskipTests

# -------- STAGE 2 - Runtime --------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o jar gerado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]