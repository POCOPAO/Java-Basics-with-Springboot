package com.bpi.java.training.dashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bpi.java.training.dashboard.dto.UserDTO;
import com.bpi.java.training.dashboard.model.User;
import com.bpi.java.training.dashboard.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO){
		try {
			User user = userService.createUser(userDTO);
			return ResponseEntity.ok("User created with username: " + user.getUsername());
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
