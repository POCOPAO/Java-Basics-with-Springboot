# Spring Security changes applied

## What was added
- Spring Security dependency
- Database-backed authentication using `app_users` table
- Role-based access control for `ROLE_ADMIN` and `ROLE_USER`
- Method-level security with `@PreAuthorize`
- HTTP Basic authentication for quick API testing
- BCrypt password hashing
- Stateless session management for API best practice
- JSON responses for 401, 403, 404, and business errors
- `/api/auth/me` endpoint to read the current authenticated user from the security context

## Default accounts
- admin / admin123
- user / user123

## Endpoint access
- `GET /api/books` -> ADMIN or USER
- `POST /api/books/addBook` -> ADMIN only
- `PATCH /api/books/updateBook` -> ADMIN only
- `DELETE /api/books/delBook` -> ADMIN only
- `POST /api/books/borrowBook?bookId=1` -> USER only
- `POST /api/books/returnBook?bookId=1` -> USER only
- `GET /api/auth/me` -> any authenticated user

## Important change
Borrow and return now use the **currently logged-in user** from Spring Security.
You no longer need to pass `userId` in the request.
