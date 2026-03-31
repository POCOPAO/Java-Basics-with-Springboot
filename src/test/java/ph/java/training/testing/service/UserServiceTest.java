package ph.java.training.testing.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;

import ph.java.training.testing.model.User;
import ph.java.training.testing.repository.UserRepository;

@Service
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ 1. Successful user creation
    @Test
    void testCreateUser_Success() {
        String username = "paolo";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(new User(1L, username));

        User newUser = userService.createUser(username);

        assertNotNull(newUser);
        assertEquals("paolo", newUser.getUsername());
    }

    // ✅ 2. Creating a duplicate user should fail
    @Test
    void testCreateUser_DuplicateUsername() {
        String username = "paolo";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User(1L, username)));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(username);
        });

        assertEquals("Username already exists", ex.getMessage());
    }

    // ✅ 3. Successful role assignment
    @Test
    void testAssignRole_Success() {
        String username = "paolo";
        User user = new User(1L, username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.assignRole(username, "ADMIN");

        assertEquals("ADMIN", user.getRole());
        verify(userRepository).save(user);
    }

    // ✅ 4. Assigning a role to a non-existing user should fail
    @Test
    void testAssignRole_UserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.assignRole("ghost", "ADMIN");
        });

        assertEquals("User not found", ex.getMessage());
    }
}