#!/bin/bash

# export the enviroment variables


export $(cat .env | xargs)

# Build the application (if necessary)
./mvnw clean package -DskipTests  # For Maven

# build image 
sudo docker build -t lexilearn-media-distributor .

sudo docker compose up
 # Run the Spring Boot app
# java -jar target/*.jar  # Adjust path if necessary

