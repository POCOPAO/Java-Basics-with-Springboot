# UserService Testing Project

This is a sample Spring Boot Maven project that covers all three exercises:

- **Exercise 1:** unit tests for `UserService`
- **Exercise 2:** integration tests for `UserService` using `@SpringBootTest`
- **Exercise 3:** web layer tests for `UserController` using `@WebMvcTest`

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 in-memory database
- JUnit 5
- Mockito
- MockMvc

## Features

- Create users
- Prevent duplicate usernames
- Create roles
- Assign roles to users
- Validation on request DTOs
- Global exception handling

## Exercise Mapping

### Exercise 1 - Unit Testing
`src/test/java/com/example/userservice/service/UserServiceUnitTest.java`

Covers:
- successful user creation
- duplicate username failure
- successful role assignment
- missing user failure
- missing role failure

### Exercise 2 - Integration Testing
`src/test/java/com/example/userservice/service/UserServiceIntegrationTest.java`

Uses:
- `@SpringBootTest`
- real repositories
- H2 database
- database cleanup in `@BeforeEach`

### Exercise 3 - Web Layer Testing
`src/test/java/com/example/userservice/controller/UserControllerTest.java`

Uses:
- `@WebMvcTest(UserController.class)`
- mocked `UserService`
- `MockMvc`
- DTO validation with `@Valid`
- global exception handler testing for `UsernameAlreadyExistException`

Covers:
- positive create user scenario
- invalid request body / validation failure
- duplicate username handled by controller advice

## Project Structure

```text
src/main/java/com/example/userservice
├── controller
│   └── UserController.java
├── dto
│   ├── CreateRoleRequest.java
│   └── CreateUserRequest.java
├── entity
│   ├── Role.java
│   └── User.java
├── exception
│   ├── DuplicateUsernameException.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UsernameAlreadyExistException.java
├── repository
│   ├── RoleRepository.java
│   └── UserRepository.java
└── service
    ├── UserService.java
    └── UserServiceImpl.java
```

## How to run tests

```bash
mvn test
```

## How to run the application

```bash
mvn spring-boot:run
```

## API Endpoints

### Create user
`POST /api/users`

Example request:

```json
{
  "username": "john",
  "password": "password123"
}
```

### Create role
`POST /api/roles`

Example request:

```json
{
  "name": "ADMIN"
}
```

### Assign role to user
`POST /api/users/{username}/roles/{roleName}`

Example:

`POST /api/users/john/roles/ADMIN`

### Get user by username
`GET /api/users/{username}`

## Notes

- `password` is write-only in API responses
- the project is designed for teaching unit, integration, and web layer testing
- the tests follow the AAA pattern where appropriate
