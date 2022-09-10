## Zip Splitter

A utility that split compressed files into a set of compressed files such that each compressed file doesn't
exceed a maximum size. It is written in Java 11 and build using Gradle.

## Getting Started

### Prerequisites

If you have not got Java installed already, please install it, or alternatively you can run the application using Docker.

### Setup and Usage

Either of the following methods can be used to set up the application as desired.

#### 1. Building and Running a JAR

* Build the JAR file using gradle, the output *zip-splitter.jar* would be in /build/libs directory in the project folder.
  ```sh
  gradle clean build 
   ```

* Run the application with desired arguments.
    1. To split zip:

  ```sh
  java -jar build/libs/zip-splitter.jar -zipPath=<zip-path> -maxSizeInMB=<max-size-in-MB> -outputPath=<output-path>
   ```
   
  Example command:
  ```sh
  java -jar build/libs/zip-splitter.jar -zipPath=<path-to-zip-splitter-project-dir>/test-resources/zip-before-split/test.zip -maxSizeInMB=150 -outputPath=<path-to-zip-splitter-project-dir>/test-resources/zip-after-split
  ```

<br></br>

#### 2. Building and Running using Docker

The idea is to run the application leveraging docker volumes for mapping host directories to be split.

* Build the docker image from project directory
    ```sh
   sudo docker build -t zip-splitter .
   ```
  The build might take some time as it would download the image and gradle dependencies. Verify that the build was
  successful and proceed.
  <br><br>
* Run the application with desired args.
    1. To split zip:
    ```sh
    sudo docker run -v path_input_dir:/mnt/source -v path_out_dir:/mnt/dest zip-splitter java -jar /build/libs/zip-splitter.jar -zipPath=<zip-path> -maxSizeInMB=<max-size-in-MB> -outputPath=<output-path> 
    ```

<br></br>

***NOTE*** :

1. By default, the application runs with a single thread.

***TODOs***:
* All the files in a zip to be recompressed in a multithreaded fashion.

<br></br>
