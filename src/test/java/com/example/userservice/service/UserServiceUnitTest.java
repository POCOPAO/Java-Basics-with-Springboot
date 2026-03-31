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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        user = new User("john", "password123");
        user.setId(1L);

        adminRole = new Role("ADMIN");
        adminRole.setId(100L);
    }

    @Test
    @DisplayName("Exercise 1 - should create user successfully")
    void createUser_success() {
        // Arrange
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // Act
        User createdUser = userService.createUser("john", "password123");

        // Assert
        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        assertEquals("john", createdUser.getUsername());
        verify(userRepository).existsByUsername("john");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Exercise 1 - should fail when username already exists")
    void createUser_duplicateUsername_throwsException() {
        // Arrange
        when(userRepository.existsByUsername("john")).thenReturn(true);

        // Act
        UsernameAlreadyExistException exception = assertThrows(
                UsernameAlreadyExistException.class,
                () -> userService.createUser("john", "anotherPassword")
        );

        // Assert
        assertEquals("Username already exists: john", exception.getMessage());
    }

    @Test
    @DisplayName("Exercise 1 - should assign role successfully")
    void assignRoleToUser_success() {
        // Arrange
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.assignRoleToUser("john", "ADMIN");

        // Assert
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getRoles().size());
        assertTrue(updatedUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
        verify(userRepository).findByUsername("john");
        verify(roleRepository).findByName("ADMIN");
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Exercise 1 - should fail when assigning role to missing user")
    void assignRoleToUser_userNotFound_throwsException() {
        // Arrange
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser("missingUser", "ADMIN")
        );

        // Assert
        assertEquals("User not found: missingUser", exception.getMessage());
    }

    @Test
    @DisplayName("Exercise 1 - should fail when role does not exist")
    void assignRoleToUser_roleNotFound_throwsException() {
        // Arrange
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser("john", "ADMIN")
        );

        // Assert
        assertEquals("Role not found: ADMIN", exception.getMessage());
    }
}
