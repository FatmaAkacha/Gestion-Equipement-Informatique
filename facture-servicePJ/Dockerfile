# Étape 1 : Utiliser une image de base Java 17
FROM openjdk:17-jdk-slim

# Étape 2 : Ajouter un argument pour le fichier JAR
ARG JAR_FILE=target/facture-servicePJ-0.0.1-SNAPSHOT.jar

# Étape 3 : Copier le JAR dans l'image Docker
COPY ${JAR_FILE} app.jar

# Étape 4 : Exposer le port de votre application (par défaut 8080)
EXPOSE 7000 

# Étape 5 : Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
