# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jre-alpine

#Open a folder inside that container and set it as the working directory
WORKDIR /app 
# copy the jar file from the host machine to the container
COPY target/friend-app-1.0.0.jar app.jar
#tells to use port 8080
EXPOSE 8080
#says "open this and run java -jar app.jar" when the container starts
ENTRYPOINT [ "java", "-jar", "app.jar" ]