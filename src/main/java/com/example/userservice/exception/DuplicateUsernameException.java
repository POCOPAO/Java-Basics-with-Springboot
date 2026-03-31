package com.example.userservice.exception;

@Deprecated
public class DuplicateUsernameException extends UsernameAlreadyExistException {
    public DuplicateUsernameException(String username) {
        super(username);
    }
}
