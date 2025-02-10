FROM openjdk:21
RUN mkdir -p /usr/local/codeset-authoring-tool
COPY codeset-authoring-tool.jar /usr/local/codeset-authoring-tool
CMD ["java", "-Xmx900m", "-Xss256k", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/local/codeset-authoring-tool/codeset-authoring-tool.jar"]