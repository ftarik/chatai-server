# ChatAI Backend - Project Summary

## Project Overview

ChatAI Backend is a production-ready Spring Boot application that provides a REST API gateway for interacting with OpenAI's GPT models. It serves as a secure proxy between clients and the OpenAI API, adding essential features like user management, token quota enforcement, and comprehensive audit logging.

### What Makes This Project Community-Ready

✅ **Comprehensive Documentation** - README with full setup instructions, API docs, and troubleshooting  
✅ **Clean Code with Comments** - All major classes have detailed Javadoc comments  
✅ **Contributing Guidelines** - Clear CONTRIBUTING.md for new contributors  
✅ **Security Policies** - SECURITY.md with vulnerability reporting procedures  
✅ **Code of Conduct** - Community standards in CODE_OF_CONDUCT.md  
✅ **Open Source License** - MIT License for permissive use  
✅ **Multi-profile Support** - Easy deployment to dev, staging, and production  
✅ **Structured Codebase** - Well-organized package structure following Spring conventions  

## Architecture Overview

### Layered Architecture

```
┌─────────────────────────────────────────┐
│  REST Controllers (HTTP Endpoints)      │ CallerController, LogController
├─────────────────────────────────────────┤
│  Business Logic (Services)              │ CallerService, LogService
├─────────────────────────────────────────┤
│  Data Access Layer (DAO Services)       │ UserDaoService, LogDaoService
├─────────────────────────────────────────┤
│  Repositories (Spring Data JPA)         │ UserRepository, LogRepository
├─────────────────────────────────────────┤
│  Database (PostgreSQL)                  │ user_entity, log_entity tables
└─────────────────────────────────────────┘
```

### Component Responsibilities

| Component | Responsibility |
|-----------|-----------------|
| **Controllers** | Handle HTTP requests, validate input, route to services |
| **Services** | Implement business logic, coordinate with OpenAI API |
| **DAO Services** | Provide database abstraction layer |
| **Repositories** | Perform actual database operations |
| **Entities** | Represent database tables and domain models |
| **Filters** | Intercept requests for authentication and CORS |
| **Configs** | Spring configuration beans and security setup |
| **Utils** | Helper classes for hashing, encryption, context management |

## Key Features Implementation

### 1. User Key Generation
- **File**: `CallerServiceImpl.generateKey()`
- **Feature**: Creates unique SHA-256 hashed keys for API authentication
- **Database**: Stored in `UserEntity.key` column
- **Token Quota**: Each user gets default 500 token limit

### 2. Single-turn Conversation
- **Endpoint**: `POST /chatai/requests`
- **Implementation**: Forwards single message to OpenAI GPT-3.5-turbo
- **Token Tracking**: Updates `UserEntity.totalTokens` with consumed tokens
- **Error Handling**: Returns 409 Conflict if quota exceeded

### 3. Multi-turn Conversation  
- **Endpoint**: `POST /chatai/requests/continue`
- **Implementation**: Sends full conversation history to OpenAI
- **Context Management**: Maintains conversation continuity
- **Token Efficiency**: Shares token pool across conversation

### 4. Request Logging
- **Endpoint**: `POST /chatai/log`
- **Storage**: Persists to `LogEntity` table
- **Encryption**: Logs can be encrypted before transmission
- **Auditing**: Full audit trail of user actions

## Database Design

### UserEntity Table
```sql
CREATE TABLE user_entity (
  id BIGSERIAL PRIMARY KEY,
  key VARCHAR(255) UNIQUE,                    -- API authentication key
  total_tokens BIGINT NOT NULL,               -- Current usage
  total_tokens_authorized BIGINT NOT NULL,    -- Quota limit
  created_date TIMESTAMP NOT NULL,            -- Creation timestamp
  created_by VARCHAR(255) NOT NULL,           -- Creator identifier
  modified_date TIMESTAMP,                    -- Last modification timestamp
  modified_by VARCHAR(255)                    -- Last modifier identifier
);
```

### LogEntity Table
```sql
CREATE TABLE log_entity (
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR(255),                      -- Action category
  user_action_trigger VARCHAR(255),           -- User identifier
  action_date TIMESTAMP,                      -- Action timestamp
  action_type VARCHAR(255),                   -- CREATE, UPDATE, DELETE, READ
  action_result VARCHAR(255),                 -- Success/failure result
  action_desc VARCHAR(255)                    -- Detailed description
);
```

## Security Implementation

### Authentication Flow
1. Client receives API key from `GET /chatai/requests`
2. Client includes key in `Authorization` header
3. `JwtAuthenticationFilter` validates header and creates user context
4. `UserContextHolder` stores context in thread-local storage
5. Service verifies user quota before processing request
6. Context is cleared after request completes (prevents memory leaks)

### Authorization Rules
```
Public Endpoints (no auth required):
- GET  /chatai/requests                  (generate key)
- POST /chatai/requests                  (single-turn, but requires key in header)
- POST /chatai/requests/continue         (multi-turn, but requires key in header)
- POST /chatai/log                       (frontend logging)
- POST /chatai/system                    (system logging)

Protected Endpoints (auth required):
- All other Spring Actuator endpoints
```

### Token Quota System
- User starts with 500 authorized tokens
- Each API call consumes tokens based on OpenAI usage
- Requests rejected with HTTP 409 Conflict if `totalTokens > totalTokensAuthorized`
- Administrator can modify quotas via database

## Dependencies & Technology Stack

### Core Framework
- **Spring Boot** 2.6.14 - Application framework
- **Spring Security** - Authentication/authorization
- **Spring Data JPA** - Database ORM
- **Hibernate** - JPA implementation

### Database
- **PostgreSQL** 12+ - Production database
- **H2** (test profile) - In-memory testing database

### External APIs
- **OpenAI Chat API** - GPT-3.5-turbo model integration
- **OkHttp3** - HTTP client for OpenAI requests

### Utilities
- **Lombok** - Reduce boilerplate code
- **Logback** - Structured logging with Logstash encoding
- **MapStruct** - Object mapping
- **JWT (JJWT)** - JSON Web Token support

### Build & Testing
- **Maven 3.6+** - Build automation
- **JUnit 4** - Unit testing framework
- **Jacoco** - Code coverage reporting
- **SonarQube** - Code quality analysis

## Configuration Management

### Application Profiles
```yaml
# Development (default)
src/main/resources/application-dev.yml

# Staging
src/main/resources/application-staging.yml

# Production
src/main/resources/application-prod.yml

# Testing
src/test/resources/application-test.yml
```

### Environment Variables (Production)
```bash
SPRING_PROFILES_ACTIVE=prod
OPENAI_API_KEY=sk-your-key
DATABASE_URL=jdbc:postgresql://host:5432/db_chatai
DATABASE_USER=postgres
DATABASE_PASSWORD=secure_password
ENCRYPTION_KEY=ld5gha8d72ed45s6
```

## Build & Deployment

### Local Development
```bash
# Clone and build
git clone https://github.com/yourusername/chatai.git
cd chatai
mvn clean package -Pdev

# Run development server
java -jar target/chatai-2.0.0-RELEASE.jar --spring.profiles.active=dev
```

### Docker Deployment
```dockerfile
FROM openjdk:19-slim
COPY target/chatai-2.0.0-RELEASE.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes Deployment (Optional)
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: chatai-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: chatai
  template:
    metadata:
      labels:
        app: chatai
    spec:
      containers:
      - name: chatai
        image: chatai:2.0.0-RELEASE
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: OPENAI_API_KEY
          valueFrom:
            secretKeyRef:
              name: chatai-secrets
              key: openai-key
```

## Code Quality & Standards

### Code Style Guidelines
- **Formatting**: 2 spaces indentation, 100-char line limit
- **Naming**: PascalCase classes, camelCase methods/variables
- **Comments**: Comprehensive Javadoc for all public classes/methods
- **Testing**: Minimum 80% code coverage target

### Quality Tools
```bash
# Run tests with coverage
mvn test jacoco:report

# SonarQube analysis
mvn sonar:sonar

# Code formatting check
mvn checkstyle:check
```

## Performance Considerations

### Optimization Strategies
1. **Connection Pooling**: HikariCP for database connections
2. **Request Timeouts**: 15s connect, 30s read for OpenAI API calls
3. **Lazy Loading**: JPA entity relationships configured appropriately
4. **Caching**: Consider Redis for frequently accessed user keys
5. **Async Processing**: Future enhancement for non-blocking requests

### Load Testing Recommendations
```bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8081/chatai/requests

# Using JMeter
# Create test plan with thread group (users: 100, ramp-up: 10s, loops: 10)
```

## Monitoring & Observability

### Logging
- **Format**: Structured JSON logs with Logstash encoder
- **Output**: 
  - Console: Real-time logs with color coding
  - File: `./logs/chatai.log`
  - Archive: Daily rollover to `./logs/archived/`

### Metrics (via Spring Actuator)
```bash
# Enable all endpoints in application.yml:
management:
  endpoints:
    web:
      exposure:
        include: "*"

# Access endpoints:
GET /actuator/metrics                    # List all metrics
GET /actuator/health                     # Application health
GET /actuator/env                        # Environment properties
```

### Health Checks
```json
GET /actuator/health

Response:
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": { ... }
    }
  }
}
```

## Future Enhancement Roadmap

### Short Term (v2.1)
- [ ] Implement request rate limiting per user
- [ ] Add conversation history persistence
- [ ] Support for other OpenAI models (GPT-4, etc.)

### Medium Term (v3.0)
- [ ] WebSocket support for real-time conversations
- [ ] User subscription tiers with tier-based quotas
- [ ] Admin dashboard for user management
- [ ] Integration with other AI models (Claude, LLaMA)

### Long Term (v4.0)
- [ ] Advanced caching layer (Redis integration)
- [ ] Analytics and usage insights dashboard
- [ ] Federated authentication (OAuth2, SAML)
- [ ] Multi-tenancy support
- [ ] Usage predictions and recommendations

## Contributing & Support

### How to Contribute
1. Read [CONTRIBUTING.md](CONTRIBUTING.md)
2. Follow the [Code of Conduct](CODE_OF_CONDUCT.md)
3. Create feature branches from `main`
4. Submit pull requests with clear descriptions
5. Ensure tests pass and coverage > 80%

### Getting Help
- **Issues**: Use GitHub Issues for bugs and features
- **Discussions**: Use GitHub Discussions for questions
- **Security**: Email tarikfamil@gmail.com for security issues
- **Documentation**: Check README.md and inline comments

### Community
- Star the repo if you find it useful
- Share feedback and suggestions
- Contribute improvements and bug fixes
- Help answer questions from other users

## License & Attribution

This project is licensed under the MIT License - see [LICENSE](LICENSE) for details.

**Original Author**: Tarik FAMIL (tarikfamil@gmail.com)

## Version History

| Version | Release Date | Major Features |
|---------|--------------|----------------|
| 2.0.0   | 2024-01-15   | Multi-turn conversations, improved logging, CORS support |
| 1.0.0   | 2023-03-05   | Initial release, single-turn API, key generation |

---

**Made with ❤️ for the community. Contributions welcome!**

For the latest updates, visit: https://github.com/yourusername/chatai
