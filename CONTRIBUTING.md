# Contributing to ChatAI

First off, thank you for considering contributing to ChatAI! It's people like you that make ChatAI such a great tool.

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to tarikfamil@gmail.com.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the issue list as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

* **Use a clear and descriptive title**
* **Describe the exact steps which reproduce the problem** in as many details as possible
* **Provide specific examples to demonstrate the steps**
* **Describe the behavior you observed after following the steps**
* **Explain which behavior you expected to see instead and why**
* **Include screenshots and animated GIFs if possible**
* **Include your environment details**: Java version, OS, Spring Boot version, etc.

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

* **Use a clear and descriptive title**
* **Provide a step-by-step description of the suggested enhancement**
* **Provide specific examples to demonstrate the steps**
* **Describe the current behavior** and **the expected behavior**
* **Explain why this enhancement would be useful**

### Pull Requests

* Follow the Java style guide (see below)
* Include appropriate test cases
* Update documentation as needed
* End all files with a newline
* Avoid platform-dependent code

## Getting Started with Development

### Prerequisites

- Java 19 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Git

### Setting Up Your Development Environment

1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/chatai.git
   cd chatai
   ```

3. Add the upstream repository:
   ```bash
   git remote add upstream https://github.com/ORIGINAL_OWNER/chatai.git
   ```

4. Create a virtual environment (optional but recommended):
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

5. Install dependencies:
   ```bash
   mvn clean install
   ```

6. Set up PostgreSQL:
   ```bash
   createdb db_chatai_dev
   ```

7. Configure your development environment:
   - Copy `src/main/resources/application-dev.yml` to your local machine
   - Update database credentials and OpenAI API key

8. Run the application:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
   ```

### Development Workflow

1. Create a new branch for your feature:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. Make your changes and commit them:
   ```bash
   git add .
   git commit -m "Add feature: brief description of changes"
   ```

3. Keep your branch in sync with upstream:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

4. Push to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```

5. Create a Pull Request on GitHub with a clear title and description

## Java Style Guide

### Naming Conventions

* **Classes**: PascalCase (e.g., `UserEntity`, `CallerService`)
* **Methods**: camelCase (e.g., `getUserKey()`, `askRequest()`)
* **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_TOKEN_QUOTA`, `API_TIMEOUT`)
* **Variables**: camelCase (e.g., `userEntity`, `totalTokens`)

### Code Formatting

* Use 2 spaces for indentation (not tabs)
* Maximum line length: 100 characters
* Add proper spacing around operators
* Use meaningful variable names

### Javadoc Comments

Every public class and method must have proper Javadoc comments:

```java
/**
 * Brief description of what the class/method does.
 * 
 * More detailed explanation if needed. Can span multiple lines.
 * 
 * @param paramName Description of the parameter
 * @return Description of the return value
 * @throws ExceptionType Description of when this exception is thrown
 * @since 1.0
 * @author Your Name
 */
public ResponseEntity<KeyResource> generateKey(String userId) {
    // Implementation
}
```

### Example: Well-formatted method

```java
/**
 * Generates a unique user key for API access.
 * 
 * If the user already has a key, returns the existing key.
 * Otherwise, creates a new user and generates a SHA-256 hashed key.
 * 
 * @param userId the unique identifier of the user
 * @return ResponseEntity containing the generated/existing key
 * @throws ExpectationFailedException if key generation fails
 */
@Override
public ResponseEntity<KeyResource> generateKey(String userId) {
  log.debug("Start generating key for user: {}", userId);
  
  UserEntity userEntity = null;
  try {
    userEntity = userDaoService.findOne(
      Specification.where(UserSpecifications.withId(Long.valueOf(userId)))
    );
  } catch (Exception e) {
    log.debug("User not found, creating new user: {}", userId);
  }
  
  if (userEntity == null) {
    userEntity = new UserEntity();
    userEntity.setTotalTokensAuthorized(500L);
    userEntity.setTotalTokens(0L);
    userEntity = userDaoService.save(userEntity);
    
    try {
      String key = HasherUtil.getHashedKey(userEntity.getId());
      userEntity.setKey(key);
      userDaoService.save(userEntity);
      return new ResponseEntity<>(new KeyResource(key), HttpStatus.OK);
    } catch (NoSuchAlgorithmException e) {
      throw new ExpectationFailedException("Failed to generate key", e);
    }
  }
  
  return new ResponseEntity<>(new KeyResource(userEntity.getKey()), HttpStatus.OK);
}
```

## Testing

### Unit Tests

* Write unit tests for all new functionality
* Test both happy paths and error cases
* Use meaningful test method names
* Aim for at least 80% code coverage

Example test structure:
```java
@Test
public void shouldGenerateKeyForNewUser() {
    // Arrange
    UserEntity newUser = new UserEntity();
    
    // Act
    ResponseEntity<KeyResource> response = callerService.generateKey();
    
    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().getKey());
}
```

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=CallerServiceImplTest

# Run with coverage report
mvn test jacoco:report
```

## Commit Messages

Follow these guidelines for commit messages:

* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
* Limit the first line to 72 characters or less
* Reference issues and pull requests liberally after the first line

Example:
```
Add support for multi-turn conversations

- Implement continueConversation method in CallerService
- Add ContinueMessageResourcePost DTO
- Update CallerController with new endpoint
- Add unit tests for conversation continuity

Fixes #123
```

## Documentation

* Update README.md with any new features
* Add inline comments for complex logic
* Update API documentation for new endpoints
* Include examples for new features

## Question or Need Help?

* Check existing issues and discussions
* Email tarikfamil@gmail.com
* Open a new discussion on GitHub

## Recognition

Contributors will be recognized in:
* The CONTRIBUTORS file
* Release notes for significant contributions
* GitHub contributors page

## Additional Notes

### Issue and Pull Request Labels

* `bug` - Something isn't working
* `enhancement` - New feature or request
* `documentation` - Improvements or additions to documentation
* `good first issue` - Good for newcomers
* `help wanted` - Extra attention is needed
* `question` - Further information is requested
* `wontfix` - This will not be worked on

## References

* [GitHub's guide to contributing](https://guides.github.com/activities/contributing-to-open-source/)
* [Atom Contributing Guidelines](https://github.com/atom/atom/blob/master/CONTRIBUTING.md)
* [Spring Boot Contributing Guide](https://github.com/spring-projects/spring-boot/blob/main/CONTRIBUTING.md)

---

Thank you for contributing to ChatAI! 🎉
