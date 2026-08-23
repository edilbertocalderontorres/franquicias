FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY *.jar franquicias-reactivo.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser
ENTRYPOINT [ "/bin/sh", "-c", "/opt/java/openjdk/bin/java $JAVA_OPTS  -jar /franquicias-reactivo.jar" ]
