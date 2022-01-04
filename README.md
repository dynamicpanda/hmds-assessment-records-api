# HMDS Senior Application Developer Coding Assessment - Records API

## Summary
This project contains a Java REST API developed as part of a coding assessment for the HM Document Solutions Senior Application Developer position.

The API is built to fulfill a Swagger API definition provided as part of the assessment requirements and contains GET, PUT, and DELETE methods for retrieving and managing records on a system. The project also includes JUnit tests to validate the functionality of the API.

Records involve a unique identification number and have associated data in JSON format.

## Requirements
> ### Summary
> This code assessment is to construct an API from a Swagger API definition. This is a record API which provides functionality to retrieve and manage record stored on a system.
> The applicant should be prepared to discuss his or her thought process and implementation during the interview.
> 
> ### Requirements
> Write a REST API in Java that fulfills the Swagger API definition in the corresponding Swagger YAML file. Write tests in Java to verify the API’s functionality.
> 
> Notes:
> - The REST API and corresponding tests must be written in > Java.
> - Any data which the API needs to store should be contained within the working directory of the application.
> - Feel free to provide associated documentation and/or to > add further documentation to the existing Swagger YAML file.
> 
> ### Submission
> Code should be uploaded to your source control platform of choice (Gitlab/Github/Bitbucket) and made available to the hiring team prior to the interview. Follow the proper source control pipeline showing branching/commit/push/merge etc.

## Usage
The Java application is build with Gradle. To build and run the application, run the following within the project folder:

```
./gradlew clean build

./gradlew bootRun
```

**Note**: `./gradlew clean build` also runs all defined tests.

The API may then be used under the base URL `http://localhost:8080`.

## Repository Contents
### docs
Supplementary documentation for the project. Namely, the requirement documents provided by the hiring team.

### gradle
Contains the Gradle wrapper and properties with which to build the application.

### src/main
The source code for the application.

### src/test
JUnit tests for the application.

## Design
The application is written using the Spring framework, namely the `RestController` for the API class definition.

The Spring framework provides helpful annotations for readability and simpler implementation, and also includes a test framework for running API tests.

A single RecordsAPI class exists to hold the methods responsible for exposing GET/PUT/DELETE functionality, and a ServiceRunner application initializes the API.
