FROM gradle:7.5.1-jdk11
WORKDIR /app
COPY . .
ENV JAR_ARGS=""
ENTRYPOINT java -jar zip-splitter.jar $JAR_ARGS
RUN gradle clean build