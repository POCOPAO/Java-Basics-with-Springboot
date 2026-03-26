package com.bpi.java.training.dashboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationDemoController {

	@GetMapping("/public")
	public String publicPage() {
		return "Public";
	}
	
	@GetMapping("/profile")
	@PreAuthorize("hasRole('USER')")
	public String profilePage(){
		return "Profile";
	}
	
	@GetMapping("/profile/{username}")
	@PreAuthorize("hasRole('USER')")
	public String profilePage(@PathVariable(name = "username") String username) {
		return "Profile of " + username;
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminPage() {
		return "Admin";
	}
}
