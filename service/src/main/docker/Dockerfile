FROM adoptopenjdk:11-jdk-openj9-bionic
MAINTAINER Ian.Ibbotson@semweb.co
VOLUME /tmp
ADD service-0.1.war authsvc.war
RUN sh -c 'touch /authsvc.war'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/authsvc.war"]
EXPOSE 8080/tcp
