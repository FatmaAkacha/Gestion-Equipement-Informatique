# Étape 1 : Utilisation de l'image Maven pour la construction
FROM maven:latest AS build


# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier POM (pom.xml) et le dossier source
COPY pom.xml ./
COPY src ./src/

# Exécuter la commande Maven pour construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Utilisation de l'image JRE pour l'exécution (openjdk:17-slim)
FROM openjdk:17-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré depuis l'étape précédente
COPY --from=build /app/target/eureka-discovery-service-0.0.1-SNAPSHOT.jar /app/eureka-server.jar

# Exposer le port 8761 (par défaut pour Eureka)
EXPOSE 8761

# Commande pour exécuter l'application Eureka
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]