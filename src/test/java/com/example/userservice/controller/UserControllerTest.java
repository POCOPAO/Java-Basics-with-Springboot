package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.User;
import com.example.userservice.exception.GlobalExceptionHandler;
import com.example.userservice.exception.UsernameAlreadyExistException;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Exercise 3 - should create user successfully")
    void createUser_success() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("john");
        request.setPassword("password123");

        User savedUser = new User("john", "password123");
        savedUser.setId(1L);

        when(userService.createUser("john", "password123")).thenReturn(savedUser);

        // Act + Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("Exercise 3 - should return 400 when request body is invalid")
    void createUser_validationFailure_returnsBadRequest() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("");
        request.setPassword("123");

        // Act + Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.username").value("Username is required"))
                .andExpect(jsonPath("$.validationErrors.password").value("Password must be between 8 and 100 characters"));

        verify(userService, never()).createUser(anyString(), anyString());
    }

    @Test
    @DisplayName("Exercise 3 - should return 400 when username already exists")
    void createUser_duplicateUsername_returnsBadRequest() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("john");
        request.setPassword("password123");

        when(userService.createUser("john", "password123"))
                .thenThrow(new UsernameAlreadyExistException("john"));

        // Act + Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Username already exists: john"));
    }
}
