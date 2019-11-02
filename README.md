

# Testing the docker container

./gradlew clean build docker
docker run -p 8080:8080 -i -t semweb/authsvc


