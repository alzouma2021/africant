# Etape 1 : Construire l'application avec Maven
FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /app

# Copier seulement le pom.xml et installer les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier les sources
COPY src src

# Construire l'application
RUN mvn package -DskipTests

# Etape 2 : Créer l'image de l'application
FROM openjdk:11-jre-slim
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar africanbus.jar

# Exposer le port sur lequel l'application va tourner
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-jar", "africanbus.jar"]
