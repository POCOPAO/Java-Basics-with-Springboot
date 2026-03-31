package com.example.userservice.service;

import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;

public interface UserService {
    User createUser(String username, String password);
    Role createRole(String roleName);
    User assignRoleToUser(String username, String roleName);
    User getUserByUsername(String username);
}
