# Scam Shield Analyzer – Iteration 1 Playbook *Updated 25 April 2025*

---

## How to use this guide

This guide breaks each ticket for Iteration 1 into clear, easy‑to‑follow steps. Do the **Steps** in order. When everything in **Done when** is true, the ticket is finished and ready to merge. Unless we say otherwise, run commands from the top level of your project.

---

### T‑01  Create the GitHub repo

| Goal | *A private repo with a licence, README, and **`.gitignore`* |
| ---- | ----------------------------------------------------------- |

**Steps**

1. On GitHub, click **New repository** and make it **private**. Name it `scam-shield-analyzer`.
2. Pick the **MIT Licence** from the list.
3. Add the standard **Java** `.gitignore`.
4. Clone the repo to your computer:
   ```bash
   git clone git@github.com:<YOUR-NAMESPACE>/scam-shield-analyzer.git
   cd scam-shield-analyzer
   ```
5. Create a `README.md` with:
   - One sentence about what the service does.
   - The "Programme Aim" paragraph from the project brief.
   - A small table of the tech stack.
6. Save, stage, and push:
   ```bash
   git add .
   git commit -m "feat: init repo with README and licence"
   git push
   ```

**Done when**

- Teammates can open the repo.
- The README shows up correctly with the licence badge.

---

### T‑02  Set up Spring Boot

| Goal | *`mvn spring-boot:run`** starts the app and **`/health`** returns 200* |
| ---- | ---------------------------------------------------------------------- |

**Steps**

1. Make a starter project:
   ```bash
   mvn -B archetype:generate -DgroupId=com.scamshield -DartifactId=analyzer-service \
       -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
   ```
2. Switch to Spring Boot 3:
   - Replace the generated `pom.xml` with one that uses `spring-boot-starter-parent` 3.2.x.
   - Add these dependencies: `spring-boot-starter-web`, `spring-boot-starter-validation`, `spring-boot-starter-test`.
3. Add a tiny controller:
   ```java
   @RestController
   public class HealthController {
       @GetMapping("/health")
       public ResponseEntity<String> health() {
           return ResponseEntity.ok("OK");
       }
   }
   ```
4. Make sure `spring-boot-maven-plugin` is in the build section.
5. Run and test:
   ```bash
   mvn spring-boot:run
   curl -i http://localhost:8080/health  # Expect HTTP/1.1 200 OK
   ```
6. Commit: `feat: spring boot skeleton with /health`.

**Done when**

- `curl -s -o /dev/null -w "%{http_code}" localhost:8080/health` prints **200**.
- `HealthControllerTest` passes in CI (see T‑14).

---

### T‑03  Add API docs with OpenAPI & Swagger

| Goal | *`openapi.yaml`** is in the repo and Swagger UI works* |
| ---- | ------------------------------------------------------ |

**Steps**

1. Add `springdoc-openapi-starter-webmvc-ui` to `pom.xml`.

2. Create `src/main/resources/openapi.yaml` with a basic OpenAPI definition:

   ```yaml
   openapi: 3.0.3
   info:
     title: Scam Shield Analyzer API
     version: "1.0"
     description: Single endpoint that scores messages for scam risk.
   paths:
     /api/analyze:
       post:
         summary: Analyze a message
         requestBody:
           required: true
           content:
             application/json:
               schema:
                 $ref: "#/components/schemas/AnalyzeRequest"
               examples:
                 sample:
                   value:
                     message: "Your account has been locked"
         responses:
           "200":
             description: Successful analysis
             content:
               application/json:
                 schema:
                   $ref: "#/components/schemas/AnalyzeResponse"
   components:
     schemas:
       AnalyzeRequest:
         type: object
         required: [message]
         properties:
           message:
             type: string
             description: Text to analyze
       AnalyzeResponse:
         type: object
         properties:
           score:
             type: integer
             minimum: 0
             maximum: 100
           explanation:
             type: string
   ```

   **Why these parts matter**

   - **info** identifies the API.
   - **paths** describes the `/api/analyze` endpoint, its request body, and its 200 response.
   - **components.schemas** keeps request/response models neat and reusable.

3. Update (or create) `src/main/resources/application.yaml` so Springdoc knows where to serve the docs:

   ```yaml
   springdoc:
     api-docs:
       path: /v3/api-docs      # raw JSON
     swagger-ui:
       path: /swagger-ui.html  # interactive UI
   ```

   Restart the app and go to `http://localhost:8080/swagger-ui.html`—you should see the **Scam Shield Analyzer API** with the POST `/api/analyze` endpoint.

4. Run the app and open `http://localhost:8080/swagger-ui.html` in a browser.

5. Commit: `feat: OpenAPI spec and Swagger UI`.

**Done when**

- `openapi-generator-cli validate openapi.yaml` shows no errors.
- Swagger UI lists `/api/analyze` with the right fields.

---

### T‑04  Use an environment variable for the OpenAI key

| Goal | *App stops if **`OPENAI_API_KEY`** isn't set* |
| ---- | --------------------------------------------- |

**Steps**

1. Add `application-example.yaml` explaining you need `OPENAI_API_KEY`.
2. Make a config record:
   ```java
   @ConfigurationProperties("openai")
   public record OpenAiProperties(@NotBlank String apiKey) {}
   ```
3. Enable it with `@EnableConfigurationProperties(OpenAiProperties.class)`.
4. Write a test that expects a `BeanCreationException` when the key is missing.
5. Add setup notes to the README.
6. Commit: `feat: bind OpenAI key`.

**Done when**

- The app refuses to start without the key.
- Tests cover this at 80 % or better.

---

### T-05  Create DTOs and add validation

| Goal | *The API rejects blank or missing `message` inputs with HTTP 400 and returns a JSON error body describing the validation failure.* |
| ---- | ---------------------------------------------------------------------------------------------------------------------------------- |

**Steps**

1. Define the `AnalyzeRequest` DTO:
   - Create class `com.valor.scamshield_service.dto.AnalyzeRequest`.
   - Add a `String message` field annotated with `@NotBlank(message = "message must not be blank")`.
   - Include constructors, getters, and setters.
2. Define the `AnalyzeResponse` DTO:
   - Create class `com.valor.scamshield_service.dto.AnalyzeResponse`.
   - Add `int score` and `String explanation` fields.
   - Include constructors, getters, and setters.
3. Enable validation in the controller:
   - Annotate the controller class with `@Validated` (or use `@Valid` on the request parameter).
   - Update the endpoint method to:
     ```java
     @PostMapping("/api/analyze")
     public ResponseEntity<AnalyzeResponse> analyze(@Valid @RequestBody AnalyzeRequest request) { ... }
     ```
4. Write validation tests using MockMvc:
   - Send a POST to `/api/analyze` with:
     ```json
     { "message": "" }
     ```
   - Assert HTTP status 400.
   - Assert the response JSON contains an error list, for example:
     ```json
     {
       "timestamp": "...",
       "status": 400,
       "errors": ["message must not be blank"],
       "path": "/api/analyze"
     }
     ```
   - Also test omitting the `message` field yields HTTP 400 with a similar error response.
5. Commit changes:
   ```bash
   git add .
   git commit -m "feat: add DTOs with validation and error handling"
   ```

**Done when**

- Sending `{ "message": "" }` or omitting `message` returns HTTP 400.
- The response body is a JSON error object listing the validation failure.

---

### T‑06  Add PostgreSQL with Docker and Liquibase

| Goal | *`docker compose up`** starts Postgres and runs migration 1* |
| ---- | ------------------------------------------------------------ |

**Steps**

1. Write `docker-compose.yml` that starts `postgres:16` with `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`:
   ```yaml
   version: '3.8'
   services:
     db:
       image: postgres:16
       environment:
         POSTGRES_USER: scamshield
         POSTGRES_PASSWORD: scamshield
         POSTGRES_DB: scamshield
       ports:
         - "5432:5432"
       volumes:
         - postgres_data:/var/lib/postgresql/data
   volumes:
     postgres_data:
   ```

2. Add Liquibase dependencies to `pom.xml`:
   ```xml
   <dependency>
     <groupId>org.liquibase</groupId>
     <artifactId>liquibase-core</artifactId>
   </dependency>
   ```

3. Create `src/main/resources/db/changelog/db.changelog-master.xml`:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <databaseChangeLog
       xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                           http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.20.xsd">
       
       <changeSet id="1" author="scamshield">
           <createTable tableName="analysis">
               <column name="id" type="UUID">
                   <constraints primaryKey="true" nullable="false"/>
               </column>
               <column name="received_at" type="TIMESTAMPTZ" defaultValueComputed="now()"/>
               <column name="latency_ms" type="INT"/>
               <column name="score" type="INT">
                   <constraints checkConstraint="score BETWEEN 0 AND 100"/>
               </column>
               <column name="message" type="TEXT"/>
           </createTable>
       </changeSet>
   </databaseChangeLog>
   ```

4. Configure database connection in `application.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/scamshield
       username: scamshield
       password: scamshield
     liquibase:
       change-log: classpath:db/changelog/db.changelog-master.xml
   ```

5. Test locally:
   ```bash
   docker compose up -d
   mvn spring-boot:run  # Liquibase runs during application startup
   ```

6. Update `application.yaml` to support environment-based configuration:
   ```yaml
   spring:
     datasource:
       url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/scamshield}
       username: ${DATABASE_USERNAME:scamshield}
       password: ${DATABASE_PASSWORD:scamshield}
     liquibase:
       change-log: classpath:db/changelog/db.changelog-master.xml
   ```

7. Commit: `feat: PostgreSQL and Liquibase migration`.

**Done when**

- Docker logs say **database system is ready**.
- A second Liquibase run doesn't recreate the table (idempotent).
- The application can connect to the database and the analysis table exists.

**Note:** Setting up PostgreSQL on fly.io will be done in T-15 when deploying the application.

---

### T‑07  Wrap OpenAI calls with retries

| Goal | *Service talks to GPT‑4o and retries on errors* |
| ---- | ----------------------------------------------- |

**Steps**

1. Add the OpenAI Java SDK (`openai-java` or `com.theokanning.openai-gpt4j`).
2. Make `OpenAiClient` using the key from `OpenAiProperties`.
3. Write `GptService.analyze(String message)` that returns a probability and a short reason.
4. Add `@Retryable(maxAttempts = 3, backoff = @Backoff(multiplier = 2, delay = 500))`.
5. Use `MockWebServer` to fake 429/500 errors and test retries.
6. Commit: `feat: OpenAI wrapper with retries`.

**Done when**

- Fake 429 causes two retries then success.
- Latency is logged for each call.

---

### T‑08  Score messages

| Goal | *Combine regex hits and model chance into a 0–100 score* |
| ---- | -------------------------------------------------------- |

**Steps**

1. Make `HeuristicScorer` that looks for words like `invoice`, `urgent`, `password`. +10 points each, max 40.
2. Get GPT probability **p** (0–1).
3. Calculate:
   ```text
   score = min(100, (heuristic + p * 100) / 1.4)
   ```
4. Add parameterised JUnit tests for safe and scammy samples.
5. Commit: `feat: hybrid scoring`.

**Done when**

- Tests pass and scores look reasonable.

---

### T‑09  Explain the score

| Goal | *Give a reason in 3 sentences or fewer* |
| ---- | --------------------------------------- |

**Steps**

1. Add `spring-boot-starter-mustache`.
2. Make `explanation.mustache` with `{{reason1}}`, `{{reason2}}`.
3. Write `ExplanationRenderer` to fill in top reasons.
4. Test that output has at most 3 sentences.
5. Commit: `feat: explanation renderer`.

**Done when**

- Example: "Found a password reset link and urgent language. Model risk is high."

---

### T‑10  Hook everything together

| Goal | *POST **`/api/analyze`** returns score, explanation, and saves it* |
| ---- | ------------------------------------------------------------------ |

**Steps**

1. Create `AnalyzeController` with `/api/analyze`.
2. Inject `AnalyzeService` (uses T‑07, T‑08, T‑09).
3. Write an integration test with Testcontainers for Postgres.
4. Commit: `feat: analyze endpoint end‑to‑end`.

**Done when**

- This works:
  ```bash
  curl -X POST localhost:8080/api/analyze \
       -H "Content-Type: application/json" \
       -d '{"message":"hello"}'
  ```
  and returns a JSON like `{ "score": 2, "explanation": "..." }` and inserts a row.

---

### T‑11  Save every request

| Goal | *Store latency, score, and message in the DB* |
| ---- | --------------------------------------------- |

**Steps**

1. Make `AnalysisRepository` (`CrudRepository`).
2. Save an `Analysis` from داخل `AnalyzeService` after computing.
3. Add a test that row count goes up after a call.
4. Commit: `feat: save analyses`.

**Done when**

- `SELECT COUNT(*) FROM analysis;` increases each time.

---

### T‑12  Hit 80 % test coverage

| Goal | *Jacoco ≥ 80 %; CI fails below that* |
| ---- | ------------------------------------ |

**Steps**

1. Add Jacoco plugin.
2. Set rule: `lineCoverage > 0.80` in `pom.xml`.
3. Write more tests if needed.
4. Commit: `chore: enforce 80% coverage`.

**Done when**

- `mvn verify` fails if coverage < 80 %.

---

### T‑13  Make Docker images

| Goal | *`docker-compose.yml`** runs the app and DB together* |
| ---- | ----------------------------------------------------- |

**Steps**

1. Create a multi‑stage `Dockerfile`:
   ```Dockerfile
   FROM maven:3.9 AS build
   WORKDIR /app
   COPY . .
   RUN mvn -B package

   FROM eclipse-temurin:21-jre
   WORKDIR /app
   COPY --from=build /app/target/analyzer-service.jar app.jar
   ENTRYPOINT ["java","-jar","/app/app.jar"]
   ```
2. Add an `analyzer` service to `docker-compose.yml` that depends on the `db` service.
3. Run:
   ```bash
   docker compose up --build
   ```
   Then open `http://localhost:8080/health`.
4. Commit: `feat: containerisation`.

**Done when**

- Both containers start and the app connects to Postgres.

---

### T‑14  Set up CI

| Goal | *GitHub Actions builds, tests, and pushes Docker images* |
| ---- | -------------------------------------------------------- |

**Steps**

1. Create `.github/workflows/ci.yml` with two jobs:
   - **build**: checkout, set JDK 21, run `mvn verify`, upload Jacoco report.
   - **docker**: on push to `main`, build and push image to GHCR (`ghcr.io/<USER>/scam-shield-analyzer`).
2. Use `actions/cache` for Maven dependencies.
3. Add `mvn spotbugs:spotbugs` for static analysis.
4. Commit: `ci: add pipeline`.

**Done when**

- README shows a green CI badge.

---

### T‑15  Deploy to staging (Fly.io)

| Goal | *Public staging URL answers **`/health`** with OK* |
| ---- | -------------------------------------------------- |

**Steps**

1. Run `fly launch` to create the app (choose "Deploy Now" when prompted):
   ```bash
   fly launch --name scamshield-analyzer-staging
   ```

2. Create a Managed PostgreSQL database on fly.io (recommended):
   ```bash
   # From the Fly.io dashboard, create a new Managed Postgres cluster
   # Select region, plan and storage as appropriate
   ```
   
   Or for an unmanaged PostgreSQL database (less recommended):
   ```bash
   fly postgres create --name scamshield-db
   ```

3. Attach the database to your app:
   ```bash
   # For Managed Postgres, copy connection string from dashboard
   fly secrets set DATABASE_URL="postgres://username:password@host:port/database"
   
   # For unmanaged Postgres:
   fly postgres attach --app scamshield-analyzer-staging scamshield-db
   ```

4. Set the OpenAI API key secret:
   ```bash
   fly secrets set OPENAI_API_KEY=<your-key>
   ```

5. Deploy your application:
   ```bash
   fly deploy
   ```

6. Verify the deployment:
   ```bash
   curl https://scamshield-analyzer-staging.fly.dev/health
   ```

7. Check database connection:
   ```bash
   # For managed Postgres:
   fly mpg connect
   
   # For unmanaged Postgres:
   fly postgres connect -a scamshield-db
   
   # Then run:
   \dt  # Should show the analysis table
   SELECT COUNT(*) FROM DATABASECHANGELOG;  # Should show Liquibase has run
   ```

8. Create a GitHub action for automated deployments in `.github/workflows/deploy.yml`:
   ```yaml
   name: Deploy to Fly.io
   on:
     push:
       branches:
         - main
   jobs:
     deploy:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v4
         - uses: superfly/flyctl-actions/setup-flyctl@master
         - run: flyctl deploy --remote-only
           env:
             FLY_API_TOKEN: ${{ secrets.FLY_API_TOKEN }}
   ```

9. Commit: `feat: deploy to fly.io staging`.

**Done when**

- `curl https://scamshield-analyzer-staging.fly.dev/health` returns **OK**.
- The application can be accessed publicly with a valid SSL certificate.
- Database access works correctly in production.
