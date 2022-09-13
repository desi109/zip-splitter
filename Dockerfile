FROM gradle:7.5.1-jdk11
WORKDIR /
COPY . .
RUN gradle clean build