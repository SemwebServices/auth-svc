FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Ian.Ibbotson@semweb.co
VOLUME /tmp
ADD test2-0.1.war test2.war
RUN sh -c 'touch /test2.war'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test2.war"]
