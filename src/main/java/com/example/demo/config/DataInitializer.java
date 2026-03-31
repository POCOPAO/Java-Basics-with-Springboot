package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.DAO.User;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AppUser;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedSecurityUsers(AppUserRepository appUserRepository,
                                        UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        return args -> {
            if (appUserRepository.findByUsername("admin").isEmpty()) {
                appUserRepository.save(new AppUser(
                        "admin",
                        passwordEncoder.encode("admin123"),
                        "ROLE_ADMIN"));
            }

            if (appUserRepository.findByUsername("user").isEmpty()) {
                appUserRepository.save(new AppUser(
                        "user",
                        passwordEncoder.encode("user123"),
                        "ROLE_USER"));
            }

            if (userRepository.findByUserName("user") == null) {
                User libraryUser = new User();
                libraryUser.setName("user");
                userRepository.save(libraryUser);
            }
        };
    }
}
