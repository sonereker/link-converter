# Link Converter

![build](https://github.com/sonereker/link-converter/actions/workflows/gradle.yml/badge.svg)

An example Spring Boot REST service with endpoints to convert deep links to web urls, and vice versa according to given
page URL patterns.

Tree page types are pre-defined; `ProductPage`, `SearchPage` and a default `HomePage`. Page type of given url/deepLink is getting identified and requested deepLink/url is getting generated according to rules defined in that page type. (Please check parameterized UTs for more information about usage and sample cases.)

Project is featuring some new Java and JUnit 5 features like Records, ParameterizedTest, Local Variable Type Inference. Strategy pattern is used for separating `PageType` rules.

## Quick Run

```
docker-compose up
```

Visit http://localhost:8080/swagger-ui/#/

### Dockerless Setup

#### Requirements

* PostgreSQL 13
* Java 15

Database and user should be created according to properties in `application.yml`.

```
./gradlew build bootRun
```

## API Docs

Swagger generated API docs are available on `/swagger-ui/#/` path when application is running.
