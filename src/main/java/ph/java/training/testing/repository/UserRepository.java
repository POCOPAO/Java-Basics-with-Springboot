package ph.java.training.testing.repository;

import java.util.Optional;

import ph.java.training.testing.model.User;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
}
