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

## Run in Debug (PROD\DEV)
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