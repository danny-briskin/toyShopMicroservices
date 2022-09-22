FROM expert/docker-java-minimal:jdk8
WORKDIR /opt
ENV PORT 7000
EXPOSE 7000
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
