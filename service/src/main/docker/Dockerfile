FROM adoptopenjdk/openjdk9-openj9
MAINTAINER Ian.Ibbotson@semweb.co
VOLUME /tmp
ADD service-0.1.war authsvc.war
RUN sh -c 'touch /authsvc.war'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","//authsvc.war"]
