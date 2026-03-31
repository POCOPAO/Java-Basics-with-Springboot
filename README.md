# UserService Testing Project

A complete Spring Boot sample project for the exercises in your slides:

- **Exercise 1:** Unit test `UserService`
- **Exercise 2:** Integration test `UserService` using `@SpringBootTest`

## What is included

- Spring Boot 3 project
- H2 in-memory database
- `User` and `Role` JPA entities
- `UserService` business logic
- REST endpoints for quick manual testing
- **Unit tests** with Mockito
- **Integration tests** with `@SpringBootTest`
- Database cleanup in `@BeforeEach`
- AAA pattern in tests

## Business scenarios covered

1. Create a user successfully
2. Reject duplicate usernames
3. Assign a role to a user successfully
4. Fail when assigning a role to a missing user
5. Fail when assigning a missing role

## Package structure

```text
src
├── main
│   ├── java/com/example/userservice
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── exception
│   │   ├── repository
│   │   └── service
│   └── resources
└── test
    ├── java/com/example/userservice/service
    └── resources
```

## Run the app

```bash
mvn spring-boot:run
```

## Run the tests

```bash
mvn test
```

## Sample API calls

### Create a role

```bash
curl -X POST http://localhost:8080/api/roles   -H "Content-Type: application/json"   -d '{"name":"ADMIN"}'
```

### Create a user

```bash
curl -X POST http://localhost:8080/api/users   -H "Content-Type: application/json"   -d '{"username":"john","password":"password123"}'
```

### Assign a role to a user

```bash
curl -X POST http://localhost:8080/api/users/john/roles/ADMIN
```

### Get a user

```bash
curl http://localhost:8080/api/users/john
```

## Notes

- This project uses **H2** so it works without installing a database.
- Integration tests use `application-test.properties`.
- The database is cleaned before each integration test with `@BeforeEach`.
