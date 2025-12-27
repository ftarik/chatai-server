# ChatAI Backend - OpenAI GPT Integration Service

![Version](https://img.shields.io/badge/version-2.0.0--RELEASE-blue)
![Java](https://img.shields.io/badge/Java-19-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.14-green)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

A robust Spring Boot backend service that provides a REST API for interacting with OpenAI's GPT models. This service manages user authentication, token quotas, and conversation history while acting as a secure proxy to the OpenAI API.

## 🚀 Features

- **User Key Generation**: Secure SHA-256 based unique key generation for API consumers
- **Token Management**: Track and enforce token quotas per user to control API costs
- **Single & Multi-turn Conversations**: Support for both one-off questions and contextual conversations
- **OpenAI Integration**: Direct integration with OpenAI's GPT-3.5-turbo model
- **Request Logging**: Comprehensive audit logging of all user interactions
- **JWT Authentication**: Secure token-based authentication for API access
- **Multi-environment Support**: Dedicated configurations for dev, staging, and production
- **Error Handling**: Graceful error handling with meaningful HTTP status codes
- **CORS Support**: Cross-origin resource sharing enabled for web clients

## 📋 Prerequisites

- **Java 19+** - The application requires Java 19 or higher
- **Maven 3.6+** - For building and dependency management
- **PostgreSQL 12+** - For production data persistence
- **OpenAI API Key** - Valid API key from https://platform.openai.com/api-keys
- **Git** - For version control

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/chatai-backend.git
cd chatai-backend
```

### 2. Configure Application Properties

Create or update the configuration file for your environment:

**Development** (`src/main/resources/application-dev.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/db_chatai
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

openai:
  url: https://api.openai.com/v1
  api-key: sk-YOUR_API_KEY_HERE

server:
  port: 8081
```

**Production** (`src/main/resources/application-prod.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-prod-host:5432/db_chatai
    username: prod_user
    password: YOUR_SECURE_PASSWORD

openai:
  url: https://api.openai.com/v1
  api-key: ${OPENAI_API_KEY}  # Use environment variables

server:
  port: 8080
```

### 3. Set Up PostgreSQL Database

```bash
# Create the database
createdb db_chatai

# The application will auto-create tables on first run (ddl-auto: update)
```

### 4. Build the Application

```bash
# Build with Maven
mvn clean package

# Build for specific environment (e.g., dev)
mvn clean package -Pdev

# Build for production
mvn clean package -Pprod
```

### 5. Run the Application

```bash
# Development environment
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production environment
java -jar target/chatai-2.0.0-RELEASE.jar --spring.profiles.active=prod
```

The application will start on `http://localhost:8081` (dev) or `http://localhost:8080` (prod).

## 📚 API Documentation

### Authentication

All endpoints except `/chatai/**` and `/chatai/requests/**` require the `Authorization` header:

```
Authorization: YOUR_USER_KEY
```

### Endpoints

#### 1. Generate User Key

**GET** `/chatai/requests`

Generates a unique API key for the requesting user or returns existing key.

**Response** (200 OK):
```json
{
  "key": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6"
}
```

#### 2. Single-turn Chat Request

**POST** `/chatai/requests`

Send a single message to ChatGPT and receive a response.

**Request Header**:
```
Authorization: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
Content-Type: application/json
```

**Request Body**:
```json
{
  "content": "What is the capital of France?"
}
```

**Response** (200 OK):
```json
{
  "role": "assistant",
  "content": "The capital of France is Paris."
}
```

**Error Responses**:
- `409 Conflict` - User has exceeded token quota
- `403 Forbidden` - Invalid or missing user key
- `417 Expectation Failed` - OpenAI API communication error

#### 3. Multi-turn Conversation

**POST** `/chatai/requests/continue`

Continue a conversation with message history.

**Request Header**:
```
Authorization: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
Content-Type: application/json
```

**Request Body**:
```json
[
  {
    "role": "user",
    "content": "What is the capital of France?"
  },
  {
    "role": "assistant",
    "content": "The capital of France is Paris."
  },
  {
    "role": "user",
    "content": "What is its population?"
  }
]
```

**Response** (200 OK):
```json
{
  "role": "assistant",
  "content": "Paris has a population of approximately 2.2 million people in the city proper..."
}
```

#### 4. Log Frontend Events

**POST** `/chatai/log`

Log client-side events and errors for audit purposes.

**Request Body**:
```json
{
  "level": 1,
  "message": "User clicked button",
  "timestamp": "2024-01-15T10:30:45.123Z",
  "fileName": "app.js",
  "lineNumber": 42,
  "columnNumber": 15
}
```

## 🔐 Security

- **JWT-based Authentication**: User keys are validated on every request
- **CORS Filtering**: Configurable cross-origin access control
- **Token Quota Enforcement**: Prevents excessive API usage
- **Encrypted Configuration**: Sensitive data stored as environment variables
- **SQL Injection Prevention**: Uses JPA specifications and parameterized queries

## 🏗️ Project Structure

```
src/main/java/fr/fgroup/chatai/
├── ChatAIApplication.java           # Main entry point
├── configs/                          # Spring configurations
│   ├── AuditingConfig.java         # JPA auditing setup
│   ├── BeanProviderConfig.java     # Bean configurations
│   ├── CorsFilter.java             # CORS filtering
│   ├── SecurityConfig.java         # Security configurations
│   └── WebConfig.java              # Web configurations
├── controllers/                      # REST controllers
│   ├── CallerController.java       # Main API interface
│   ├── LogController.java          # Logging API
│   └── impl/                       # Controller implementations
├── services/                        # Business logic
│   ├── CallerService.java         # AI interaction service
│   ├── LogService.java            # Logging service
│   └── impl/                      # Service implementations
├── dao/                            # Data access layer
│   ├── repositories/              # Spring Data JPA repositories
│   ├── services/                  # DAO services
│   └── specifications/            # JPA specifications
├── entities/                       # JPA entities
│   ├── UserEntity.java           # User model
│   ├── LogEntity.java            # Log model
│   └── base/                     # Base entity classes
├── resources/                      # DTOs and request/response models
├── enums/                          # Enumeration types
├── exceptions/                     # Custom exceptions
├── filters/                        # HTTP filters
└── utils/                          # Utility classes
```

## 📊 Database Schema

### UserEntity
```sql
CREATE TABLE user_entity (
  id BIGSERIAL PRIMARY KEY,
  key VARCHAR(255),
  total_tokens BIGINT NOT NULL,
  total_tokens_authorized BIGINT NOT NULL,
  created_date TIMESTAMP NOT NULL,
  created_by VARCHAR(255) NOT NULL,
  modified_date TIMESTAMP,
  modified_by VARCHAR(255)
);
```

### LogEntity
```sql
CREATE TABLE log_entity (
  id BIGSERIAL PRIMARY KEY,
  category VARCHAR(255),
  user_action_trigger VARCHAR(255),
  action_date TIMESTAMP,
  action_type VARCHAR(255),
  action_result VARCHAR(255),
  action_desc VARCHAR(255)
);
```

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 📈 Monitoring & Logging

The application uses **Logback** with **Logstash** encoding for structured logging:

- Logs are written to `./logs/chatai.log`
- Logs are archived daily to `./logs/archived/`
- Log level can be configured in `application.yml`

```yaml
logging:
  level:
    root: WARN
    fr.fgroup.chatai: INFO
  file:
    path: ./logs
    name: ${logging.file.path}/chatai.log
```

## 🚀 Deployment

### Docker Deployment

```dockerfile
FROM openjdk:19-slim
COPY target/chatai-2.0.0-RELEASE.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENV OPENAI_API_KEY=sk-your-key
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Environment Variables

```bash
export SPRING_PROFILES_ACTIVE=prod
export OPENAI_API_KEY=sk-your-openai-key
export DATABASE_URL=jdbc:postgresql://host:5432/db_chatai
export DATABASE_USER=postgres
export DATABASE_PASSWORD=secure_password
export ENCRYPTION_KEY=ld5gha8d72ed45s6
```

## 🐛 Troubleshooting

### Common Issues

**Issue**: `401 Unauthorized`
- **Solution**: Ensure you're sending a valid user key in the Authorization header

**Issue**: `409 Conflict` on every request
- **Solution**: Check user's token quota - contact administrator to increase limits

**Issue**: Connection timeout to OpenAI
- **Solution**: Check API key validity and network connectivity

**Issue**: Database connection errors
- **Solution**: Verify PostgreSQL is running and credentials in `application.yml` are correct

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

For detailed contribution guidelines, see [CONTRIBUTING.md](CONTRIBUTING.md).

## 📧 Support & Contact

- **Author**: Tarik FAMIL
- **Email**: tarikfamil@gmail.com
- **Issues**: Please open an issue on GitHub
- **Security**: For security issues, please email tarikfamil@gmail.com

## 🔄 Version History

### v2.0.0-RELEASE (Current)
- Multi-turn conversation support
- Improved token management
- Enhanced logging and auditing
- CORS support for web clients

### v1.0.0
- Initial release
- Single-turn API integration
- Basic user key generation

## 📚 Related Resources

- [OpenAI API Documentation](https://platform.openai.com/docs/api-reference)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT (JSON Web Token) Introduction](https://jwt.io/introduction)

## 🎯 Roadmap

- [ ] WebSocket support for real-time conversations
- [ ] User subscription tiers with different token quotas
- [ ] Integration with other AI models (Claude, LLaMA)
- [ ] Advanced caching for common queries
- [ ] Admin dashboard for user management
- [ ] Rate limiting per user
- [ ] Conversation persistence and retrieval
- [ ] Analytics and usage insights

---

**Made with ❤️ by the ChatAI Team**
