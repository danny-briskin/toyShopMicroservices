FROM expert/docker-java-minimal:jdk8
WORKDIR /opt
ENV PORT 7082
EXPOSE 7082
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
