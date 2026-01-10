## Requirements

* Java 21
* Maven 3.8+
* PostgreSQL 15+
* Git

## Setup PostgreSQL

Create database:
```
CREATE DATABASE mealplanner;
```

Update ```application-prod.properties```:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/mealplanner
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Run the application (PROD)
Use this command:
```
mvn clean spring-boot:run -D"spring-boot.run.profiles"=prod
```
This will:
* Use PostgreSQL
* Run Flyway migrations
* Start the API at http://localhost:8080

## Run in DEV (H2)
```
mvn clean spring-boot:run
```
or
```
mvn clean spring-boot:run -D"spring-boot.run.profiles"=dev
```
Open H2 console:
```
http://localhost:8080/h2-console
```

## Flyway Migrations
Flyway scripts live in:
```
src/main/resources/db/migration
```
Naming:
```
V1__init.sql
V2__add_columns.sql
V3__new_feature.sql
```