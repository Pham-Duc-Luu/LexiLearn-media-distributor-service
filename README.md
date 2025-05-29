# Introduction

**Media Distributor** is a Spring Boot application designed to handle, transport, and distribute media files (like `.mp3`, `.jpg`, `.mp4`, etc.) in a microservice architecture.

The application acts as an **important bridge** that enables services in the system to:

- Receive and process media files from users or other services
- Automatically distribute these files to cloud storage platforms such as **Amazon S3**
- Support services for analyzing, searching, and extracting media data through metadata or integration mechanisms

## Main objectives

- Ensure efficient **media file transmission and storage**
- Easily integrate with popular cloud services
- Extend media usability across the entire microservice ecosystem

## Usage examples

- A user uploads a video (`.mp4`): the system will upload this file to S3, and send metadata to content analysis services

- An AI service requests a photo from a specific user: the Media Distributor will provide a secure access path to that file on the cloud

---

> Media Distributor is an indispensable part of Modern systems require multimedia data processing with low latency and high scalability.

# Media Distributor Project Structure

This document describes the directory structure and source code organization of the **Media Distributor** application, a Spring Boot application designed to process, transport, and distribute media files in a microservice system. The project structure follows Spring Boot conventions and is organized to ensure modularity, maintainability, and extensibility.

## Directory Structure Overview

Here is the main directory structure of the project, with the main packages/directories located under `src/main/java/com` (assuming `com` is the project's `groupId`):

```
src
├── main
│   ├── java
│   │   └── com
│   │       ├── Application.java
│   │       ├── argumentResolver
│   │       ├── config
│   │       ├── controller
│   │       ├── dto
│   │       ├── enums
│   │       ├── exception
│   │       ├── filter
│   │       ├── guard
│   │       ├── interceptors
│   │       ├── model
│   │       ├── repository
│   │       ├── service
│   │       ├── Strategy
│   │       └── util
│   └── resources
│       ├── application.properties
│       └── public
└── test
    └── java
        └── com
```

## Detailed description of components

### 1. `Application.java`

- **Role**: The main class of the Spring Boot application, containing the `main` method to start the application.

- **Function**:
- Initialize the Spring Boot context.
- Configure beans and scan components in subpackages.
- **Location**: Directly in the `com` package.

- **Example**:

```java
@SpringBootApplication
public class Application {
public static void main(String[] args) {
SpringApplication.run(Application.class, args);
}
}
```

### 2. `argumentResolver`

- **Role**: Contains classes that implement `HandlerMethodArgumentResolver` to customize how Spring MVC maps request parameters to method parameters in the controller.

- **Function**:

- Handle custom parameters, e.g. extract user information from a JWT token.

- **Usage example**: Map a JWT token to a `User` object in the controller.

### 3. `config`

- **Role**: Contains Spring configuration classes (usually annotated with `@Configuration`).

- **Function**:

- Configure beans, such as AWS S3, MongoDB, Elasticsearch, or Cloudinary connections.

- Configure security, CORS, or WebClient configurations.

- **Examples**:

- Configure an AWS S3 client.

- Configure WebClient for asynchronous HTTP requests.

### 4. `controller`

- **Role**: Contains REST API controller classes (annotated `@RestController`).

- **Functionality**:

- Handles HTTP requests (GET, POST, PUT, DELETE) from clients.

- Calls services to perform business logic.

For example, APIs for uploading media files, generating pre-signed URLs for S3, or searching for media.

- **Example endpoints**:

- `/api/v1/upload`: Uploads files to S3.

- `/api/v1/search`: Searches media in Elasticsearch.

### 5. `dto`

- **Role**: Contains Data Transfer Object (DTO) classes to transfer data between tiers.
- **Function**:

- Defines data structures for API requests and responses.

- Reduces direct dependency on model classes.

- **Examples**:

- `MediaUploadRequestDTO`: Contains file information and metadata.

- `MediaResponseDTO`: Contains the URL and file information after upload.

### 6. `enums`

- **Role**: Contains enumerated constants (`enum`).

- **Function**:
- Defines fixed values, such as media file types (`MP3`, `JPG`, `MP4`), processing status, or error codes.
- **Example**:

```java
public enum MediaType {
MP3, JPG, MP4
}
```

### 7. `exception`

- **Role**: Contains custom exception classes and exception handlers (`@ControllerAdvice`).

- **Function**:
- Defines specific exceptions, such as `MediaUploadException` or `InvalidFileTypeException`.

- Handles global exceptions and returns a standardized error response.

- **Example**:
- `GlobalExceptionHandler`: Handles exceptions and returns the appropriate HTTP status code.

### 8. `filter`

- **Role**: Contains Servlet filters (`@WebFilter`) to handle requests before they reach the controller.

- **Function**:
- Validates tokens, logs requests, or checks headers.
- **Example**:
- Filter that checks for API key or JWT token.

### 9. `guard`

- **Role**: Contains guard classes to check access rights or conditions before executing logic.

- **Function**:

- Check user role or resource access rights.

- **Example**:

- `AdminGuard`: Allows only users with admin role to access endpoint.

### 10. `interceptors`

- **Role**: Contains classes implementing `HandlerInterceptor` to intercept and handle HTTP requests.

- **Function**:

- Log request processing time.

- Add custom headers to response.

- **Example**:

- `LoggingInterceptor`: Logs execution time of each request.

### 11. `model`

- **Role**: Contains entity classes that represent stored data.

- **Function**:
- Mapping to tables/collections in the database (MongoDB, PostgreSQL, or MySQL).

- For example, the `Media` class contains information about media files (name, URL, metadata).

- **Example**:

```java
@Document(collection = "media")
public class Media {
@Id
private String id;
private String fileName;
private String s3Url;
// Getters and setters
}
```

### 12. `repository`

- **Role**: Contains repository interfaces to interact with the database.

- **Function**:
- Use Spring Data to provide CRUD methods for MongoDB, Elasticsearch, or JPA.
- **Examples**:

- `MediaRepository` (MongoDB): Query and store media information.

- `MediaSearchRepository` (Elasticsearch): Search media by keyword.

### 13. `service`

- **Role**: Contains service classes that implement business logic.

- **Functionality**:
- Handles tasks such as uploading files, generating pre-signed URLs, or parsing metadata.

- Calls external repositories and services (S3, Cloudinary).

- **Examples**:

- `MediaService`: Handles media file upload and storage logic.

### 14. `Strategy`

- **Role**: Contains classes that implement the Strategy design pattern to handle different behaviors.
- **Functionality**:
- Allows you to select a file handling strategy based on the file type or storage service (S3, Cloudinary).
- **Examples**:
- `S3StorageStrategy`: Uploads files to S3.
- `CloudinaryStorageStrategy`: Uploads files to Cloudinary.

### 15. `util`

- **Role**: Contains utility classes or helper functions.
- **Functionality**:
- Provides general methods, such as format conversion, file validation, or string processing.
- **Examples**:
- `FileValidator`: Checks file format and size.
- `JwtUtils`: Handles JWT encoding/decoding.

### 16. `resources`

- **Role**: Contains configuration files and static resources.
- **Functionality**:
- `application.properties`: Application configuration (see previous configuration docs).
- `public/`: Contains static resources like HTML, CSS, JS.

## Naming conventions and organization

- **Subpackages**: Each package (`controller`, `service`, etc.) is placed under `com` (or `com.yourdomain` if `groupId` is customized).
- **Class names**:
- Controller: Ends with `Controller` (e.g. `MediaController`).
- Service: Ends with `Service` (e.g. `MediaService`).
- Repository: Ends with `Repository` (e.g. `MediaRepository`).
- DTO: Ends with `DTO` (e.g. `MediaResponseDTO`).
- **Modularity**: Each package has its own responsibility, ensuring separation of concerns.

## Benefits of the structure

- **Maintainability**: Components are clearly organized, easy to find and update.

- **Extensibility**: Support for adding new storage strategies or services without affecting existing code.

- **Reusability**: Classes in `util` and `Strategy` can be reused in other projects.

- **Testability**: The separated structure makes it easy to write unit tests for services and repositories.

## Notes

- Make sure the dependencies in `pom.xml` are compatible with the versions of MongoDB, Elasticsearch, and AWS SDKs.

- When adding new packages, update this document to stay in sync.

- Use `@ComponentScan` in `Application.java` to ensure Spring scans all subpackages.

## Conclusion

The Media Distributor project structure is designed to optimize application development and maintenance. Each package has a clear role, from handling API requests to storing and searching for media. This documentation provides a foundation for developers to understand and work with the source code effectively.

# Set up

This guide provides the steps required to set up and run the **Media Distributor** application, a Spring Boot application designed to process, transport, and distribute media files in a microservice system.

## System Requirements

Before you begin, make sure your system meets the following requirements:

- **Java**: JDK 21 (specified in `pom.xml`).
- **Maven**: Latest version (3.8.0 or higher recommended).
- **MongoDB**: Ensure you have a MongoDB instance running (if using MongoDB to store data).
- **Amazon S3**: Configure your AWS account and credentials to access S3.
- **Elasticsearch**: Install and run Elasticsearch (version compatible with `elasticsearch-java` 8.17.2).
- **IDE**: IntelliJ IDEA, Eclipse or any IDE that supports Spring Boot.

## Environment Setup

1. **Install Java 21**:

- Download and install JDK 21 from [Oracle's official site](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) or use OpenJDK.

- Configure the `JAVA_HOME` environment variable and add `bin` to `PATH`.

2. **Install Maven**:

- Download Maven from [official site](https://maven.apache.org/download.cgi) and follow the installation instructions.

- Run `mvn -v` to check the Maven version.

3. **Install MongoDB** (if using):

- Download and install MongoDB from the [official site](https://www.mongodb.com/try/download/community).

- Start MongoDB using the `mongod` command.

4. **Configure AWS S3**:

- Create an AWS account and set up credentials (Access Key and Secret Key).

- Save credentials in the `~/.aws/credentials` file or configure in the application.

5. **Install Elasticsearch**:

- Download and install Elasticsearch from the [official site](https://www.elastic.co/downloads/elasticsearch).

- Start Elasticsearch and make sure it runs on `localhost:9200`.

## Project Setup

1. **Clone the Source Code**:

- Clone or download the project source code from your repository.
- Navigate to the project directory: `cd media-distributor`.

2. **Configure `pom.xml` file**:

- The `pom.xml` file has been provided and includes all the required dependencies like Spring Boot, AWS S3 SDK, MongoDB, Elasticsearch, etc.

- Make sure there are no syntax errors in the `pom.xml` file.

3. **Install dependencies**:

- In the project directory, run the following command to download and install the dependencies:

```bash
mvn clean install
```

4. **Configure the application**:

- You can learn more in the [configuration](configuration) section

5. **Run the application**:

- Run the application using the Maven command:

```bash
mvn spring-boot:run
```

- Or, if using an IDE, run the main class of the application (e.g., `com.application.Application`).

## Test the application

- After the application starts, access the Swagger UI to test the APIs:

- URL: `http://localhost:8080/swagger-ui.html`
- Make sure the connections to MongoDB, S3, and Elasticsearch are working properly by checking the application logs.

## Note

- If you encounter errors related to the dependency version, check the compatibility in `pom.xml` and update to the latest version if necessary.
- Make sure external services (MongoDB, Elasticsearch, AWS S3) are configured correctly before running the application.
- To run the tests, use the command:

```bash
mvn test
```

## Conclusion

After completing the above steps, the Media Distributor application should be ready to process and distribute media files. If you need more information on how to deploy or extend the application, please refer to the other documents in the project.
