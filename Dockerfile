FROM eclipse-temurin:22-jre-alpine

WORKDIR /app
# Set according to the app's name and version without extension, e.g. "awesome-app-0.1.0"
ARG JAR_NAME
ENV APP_NAME=$JAR_NAME.jar
COPY build/libs/$JAR_NAME.jar $APP_NAME

RUN chown nobody:nobody /app
USER nobody

EXPOSE 8080
# The Shell version of this Entrypoint description executes Java with the PID 1 only in the alpine version of the image.
# If you change the image to the "21-jre" based on Ubuntu, defined Entrypoint won't work as PID 1
# and the Graceful Shut Down of the application will be impossible.
ENTRYPOINT java -XX:MaxRAMPercentage=70.0 -XX:ActiveProcessorCount=2 -jar $APP_NAME