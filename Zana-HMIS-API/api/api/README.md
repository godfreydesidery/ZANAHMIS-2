# Zana HMIS API

Spring Boot API for Zana HMIS.

## Prerequisites

- JDK 21
- Maven

## Build

From this directory (`api/api`):

```bash
mvn clean package
```

Produces `target/api-0.0.1-Zana-HMIS.jar`.

### Other commands

```bash
# Skip tests
mvn clean package -DskipTests

# Compile only
mvn clean compile

# Install to local Maven repo
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

Or after packaging:

```bash
java -jar target/api-0.0.1-Zana-HMIS.jar
```
