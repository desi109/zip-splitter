## Zip Splitter
A utility that split compressed files into a set of compressed files such that each compressed file doesn't
exceed a maximum size. It is written in Java 11 and build using Gradle.

:whale: DockerHub: [desi109/zip-splitter](https://hub.docker.com/u/desi109/zip-splitter/tags)

## Getting Started

### Prerequisites
If you have not got Java installed already, please install it, or alternatively you can run the application using Docker.

### Setup and Usage
Either of the following methods can be used to set up the application as desired.

#### I. Building and Running a JAR
1. Build the JAR file using gradle, the output *zip-splitter.jar* would be in /build/libs directory in the project folder.
  ```sh
  gradle clean build 
   ```
2. Run the application with desired arguments. To split zip:
  ```sh
  java -jar build/libs/zip-splitter.jar -zipPath=<zip-path> -maxSizeInMB=<max-size-in-MB> -outputPath=<output-path>
   ```
   Example command:
   ```sh
   java -jar build/libs/zip-splitter.jar -zipPath=/home/user/test-resources/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=/home/user/test-resources/zips-after-split
   ```

<br></br>

#### II. Building and Running using Docker :whale:
The idea is to run the application leveraging docker volumes for mapping host directories to be split.
1. Build the docker image from project directory:
   ```sh
   sudo docker build -t zip-splitter .
   ```
   The build might take some time as it would download the image and gradle dependencies. Verify that the build was successful and proceed.
2. Run the application with desired args. To split zip:
   ```sh
   sudo docker run -v <local-system-path-to-the-zip-folder>:/zip-before-split -v <local-system-path-to-the-output-folder>:/zips-after-split zip-splitter java -jar /build/libs/zip-splitter.jar -zipPath=/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=/zips-after-split 
   ```
   When we run docker commands with ```sudo```, we may need to change the permissions of the zip files at the end.
   ```sh
   cd <local-system-path-to-the-zip-folder>
   chmod -R 757 .
   ```
   Example command:
   ```sh
   sudo docker run -v /home/user/test-resources/zip-before-split:/zip-before-split -v /home/user/test-resources/zips-after-split:/zips-after-split zip-splitter java -jar /build/libs/zip-splitter.jar -zipPath=/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=/zips-after-split
   cd /home/user/test-resources/zips-after-split
   chmod -R 757 .
   ```

<br></br>

#### III. Pull from DockerHub and Run using Docker :whale:
The idea is to run the application leveraging docker volumes for mapping host directories to be split.
1. Pull the image from DockerHub:
   ```sh
   sudo docker pull desi109/zip-splitter:latest
   ```
2. Run the application with desired args. To split zip:
   ```sh
   sudo docker run -v <local-system-path-to-the-zip-folder>:/zip-before-split -v <local-system-path-to-the-output-folder>:/zips-after-split zip-splitter java -jar /build/libs/zip-splitter.jar -zipPath=/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=/zips-after-split 
   ```
   When we run docker commands with ```sudo```, we may need to change the permissions of the zip files at the end.
   ```sh
   cd <local-system-path-to-the-zip-folder>
   chmod -R 757 .
   ```
   Example command:
   ```sh
   sudo docker run -v /home/user/test-resources/zip-before-split:/zip-before-split -v /home/user/test-resources/zips-after-split:/zips-after-split zip-splitter java -jar /build/libs/zip-splitter.jar -zipPath=/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=/zips-after-split
   cd /home/user/test-resources/zips-after-split
   chmod -R 757 .
   ```

<br></br>

***NOTE***:
* By default, the application runs with a single thread.

<br></br>

***TODOs***:
* Add tests.
* All the files in a zip to be recompressed in a multithreaded fashion.

