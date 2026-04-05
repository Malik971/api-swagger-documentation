# Dockerfile pour Libriciel Partner API Demo
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY src ./src

# Builder l'application
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests && \
    apk del maven

# Image finale légère
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Variables d'environnement (peuvent être overridées)
ENV JWT_SECRET="VotreCleSecretePourLibricielDemoChangezLaEnProduction"
ENV JWT_EXPIRATION="86400000"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/health || exit 1

# Lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
