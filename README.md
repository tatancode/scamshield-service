# Scam Shield Analyzer

A service that analyzes messages for potential scam risk.

## Project Overview

The Scam Shield Analyzer is part of the Scam Shield programme aimed at protecting users from potential scams by analyzing message content and providing a risk score.

### Starting the Application

1. **Start the PostgreSQL database**:
   ```bash
   docker compose up -d
   ```

2. **Run the Spring Boot application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Verify the application is running**:
   ```bash
   curl http://localhost:8080/api/v1/health
   ```

### Accessing Swagger UI

The API documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

### Environment Variables

Required environment variables:
- `OPENAI_API_KEY`: API key for OpenAI (for message analysis)

Optional environment variables:
- `DATABASE_URL`: Database connection URL (default: `jdbc:postgresql://localhost:5432/scamshield`)
- `DATABASE_USERNAME`: Database username (default: `scamshield`)
- `DATABASE_PASSWORD`: Database password (default: `scamshield`)

## Testing

Run tests using:
```bash
mvn test
``` 