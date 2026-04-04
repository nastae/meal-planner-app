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

## Run the application (LOCAL)
Use this command:
```
mvn clean spring-boot:run -D"spring-boot.run.profiles"=local
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

## Run in Debug (PROD\LOCAL\DEV)
1. Open Run / Debug Configurations
2. In Program arguments, add:
   ```--spring.profiles.active=prod```
3. Apply and run debug.

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

Show applied and pending migrations:
```
mvn flyway:info
```
Apply migrations:
```
mvn flyway:migrate
```

## How to build and run Docker image

### Build your Spring Boot JAR
Your Dockerfile expects a JAR file:

```
mvn clean package
```

You should now have:

```
target/your-app.jar
```

### Build Docker image
From your project root (where Dockerfile is):

```
docker build -t my-spring-app .
```

### Run the container
Basic run:

```
docker run -p 8080:8080 my-spring-app
```

Now your app is available at:

```
http://localhost:8080
```

### Run with environment variables
Since use Neon DB and ```prod``` profile, run like this:

```
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://...neon.tech:5432/dbname?sslmode=require \
  -e DB_USER=your_user \
  -e DB_PASSWORD=your_password \
  my-spring-app
```

### Run in background
```
docker run -d -p 8080:8080 my-spring-app
```

## Reset Database (Manual)
If you need to drop all tables and enums in PostgreSQL (this will delete all data!).

### Using pgAdmin
1. Open pgAdmin and connect to your `mealplanner` database.
2. Open the **Query Tool**.
3. Run the script located at:
```
src/main/resources/sql/drop_all_tables_pg.sql
```
### Using psql (Command Line)
1. Open your terminal or PowerShell.
2. Make sure `psql` is installed and in your system PATH.
3. Run the command:
```
psql -U postgres -d mealplanner -f src/main/resources/sql/drop_all_tables_pg.sql
```

⚠️ **Warning:** Only use this in development. All data will be lost!

## Future ideas
1. Add standard formatting file and use it
2. Write CI/CD pipelines
3. Use bootstrap css frameworks for form, input, button, etc.
4. Save input data in storage that after reload on mobile phone, data wouldn't disappear, and dispappear when swtich pages 