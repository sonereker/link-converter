# Link Converter

![Build Status](https://github.com/sonereker/link-converter/actions/workflows/gradle.yml/badge.svg)

An example Spring Boot REST service with endpoints to convert deep links to web urls, and vice versa according to given
page URL patterns.

## Quick Run

```
docker-compose up
```

Visit http://localhost:8080/swagger-ui/#/

### Dockerless Setup

#### Requirements

* PostgreSQL 13
* Java 15
* Gradle 6.8.3

Database and user should be created according to properties in `application.yml`.

```
gradle clean build bootRun
```

## API Docs

Swagger generated API docs are available on `/swagger-ui/#/` path when application is running.
