#!/bin/bash

# export the enviroment variables

#export $(cat .env | xargs)

# Build the application (if necessary)
#./mvnw clean package -DskipTests  # For Maven

# Build the elasticsearch imports data
#mvn clean package -DskipTests -Pimporter

# Build the api application
mvn clean package -DskipTests

# Run the Spring Boot api app
java -jar target/*.jar  # Adjust path if necessary

