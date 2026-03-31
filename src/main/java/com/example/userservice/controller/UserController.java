package com.example.userservice.controller;

import com.example.userservice.dto.CreateRoleRequest;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@RequestBody CreateRoleRequest request) {
        return userService.createRole(request.getName());
    }

    @PostMapping("/users/{username}/roles/{roleName}")
    public User assignRole(@PathVariable String username, @PathVariable String roleName) {
        return userService.assignRoleToUser(username, roleName);
    }

    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
}
