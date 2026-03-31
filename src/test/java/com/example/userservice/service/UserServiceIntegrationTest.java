package com.example.userservice.service;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.exception.UsernameAlreadyExistException;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void cleanDatabase() {
        // Clean database before each test
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Exercise 2 - should create user successfully with real database")
    void createUser_success() {
        // Arrange
        String username = "john";
        String password = "password123";

        // Act
        User createdUser = userService.createUser(username, password);

        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(username, createdUser.getUsername());
        assertTrue(userRepository.findByUsername(username).isPresent());
    }

    @Test
    @DisplayName("Exercise 2 - should fail when creating duplicate username")
    void createUser_duplicateUsername_throwsException() {
        // Arrange
        userService.createUser("john", "password123");

        // Act
        UsernameAlreadyExistException exception = assertThrows(
                UsernameAlreadyExistException.class,
                () -> userService.createUser("john", "newPassword")
        );

        // Assert
        assertEquals("Username already exists: john", exception.getMessage());
        assertEquals(1, userRepository.count());
    }

    @Test
    @DisplayName("Exercise 2 - should assign role successfully")
    void assignRoleToUser_success() {
        // Arrange
        userService.createUser("john", "password123");
        userService.createRole("ADMIN");

        // Act
        User updatedUser = userService.assignRoleToUser("john", "ADMIN");

        // Assert
        assertNotNull(updatedUser);
        assertEquals("john", updatedUser.getUsername());
        assertTrue(updatedUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
    }

    @Test
    @DisplayName("Exercise 2 - should fail when assigning role to missing user")
    void assignRoleToUser_userNotFound_throwsException() {
        // Arrange
        roleRepository.save(new Role("ADMIN"));

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser("missingUser", "ADMIN")
        );

        // Assert
        assertEquals("User not found: missingUser", exception.getMessage());
    }

    @Test
    @DisplayName("Exercise 2 - should fail when role does not exist")
    void assignRoleToUser_roleNotFound_throwsException() {
        // Arrange
        userRepository.save(new User("john", "password123"));

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser("john", "ADMIN")
        );

        // Assert
        assertEquals("Role not found: ADMIN", exception.getMessage());
    }
}
