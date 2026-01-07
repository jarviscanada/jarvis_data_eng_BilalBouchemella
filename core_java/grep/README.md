# Java Grep App

## Introduction
The Java Grep App is a command-line application that replicates the core functionality of the Linux grep command using Java. It recursively traverses a user-specified directory, reads files line by line, and identifies lines that match a given regular expression, writing the results to an output file. The application is designed using object-oriented principles and core Java features such as file I/O, regular expressions, exception handling, and collections. Java Streams and Lambda expressions are used to efficiently process and filter data. Maven is used for project management, JUnit for testing, GitHub for version control, and Docker for packaging and deployment.

## Quick Start

### Build the Application
Clone the repository and build the project using Maven:
```
mvn clean package
```
This command generates a runnable JAR file in the target/ directory.

### Run the Application
Run the application using the following command:
```
java -jar target/grep-1.0-SNAPSHOT.jar <regex> <rootDir> <outputFile>
```

- `<regex>`: regular expression to search for
- `<rootDir>`: directory to search recursively
- `<outputFile>`: file where matching lines will be written

**Example:**

Search for lines containing both Romeo and Juliet in all files under the data directory and write results to grep.out
```
java -jar target/grep-1.0-SNAPSHOT.jar .*Romeo.*Juliet.* data grep.out
```

Search for lines containing the word error in all files under the logs directory and write results to output.txt
```
java -jar target/grep-1.0-SNAPSHOT.jar error logs output.txt
```

### Run with Docker
Build and run the application using Docker:
```
docker build -t ${docker_user}/grep .
docker run --rm \
-v $(pwd)/data:/data \
-v $(pwd)/log:/log \
${docker_user}/grep <regex> /data /log/grep.out
```

## Implementation

### Algorithm Overview

The application validates input arguments, compiles the regular expression, then recursively traverses the root directory. For each file, it reads lines one at a time, checks for regex matches, and writes matching lines to the output file before closing all resources.

```text
Pseudocode:

validate inputs
compile regex
for each file in directory:
    for each line in file:
        if match → write to output
```

### Performance Issue
Reading large files or directories into memory can cause high memory usage. This issue can be resolved by using buffered reading and streaming lines one at a time, ensuring that only a single line is stored in memory at any given moment.

## Test

Manual testing was used to verify the correctness and reliability of the application.

- Sample directories with multiple text files were created, including both matching and non-matching content.
- Various regular expression patterns were tested, ranging from simple keywords to more complex expressions.
- The application was run with different input arguments, and the output files were compared against expected results.
- Additional cases such as empty files, missing directories, and invalid inputs were tested to confirm proper error handling.

JUnit was used to test core components such as regex matching and file processing logic to ensure consistent behavior.


## Deployment
The application is containerized using Docker to ensure consistent execution across different environments. A custom Dockerfile is used to package the runnable JAR along with the required Java runtime. This allows users to run the application without installing Java or Maven locally, simplifying distribution and deployment.

## Improvement
- Enhance performance by introducing parallel file processing for large directory structures. 
- Improve error handling and user feedback for invalid input arguments or inaccessible files.
- Extend the command-line interface to support additional options such as case-insensitive searches and matching multiple regular expression patterns.


