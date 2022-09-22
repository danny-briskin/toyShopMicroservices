FROM expert/docker-java-minimal:jdk8
WORKDIR /opt
ENV PORT 7081
EXPOSE 7081
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
