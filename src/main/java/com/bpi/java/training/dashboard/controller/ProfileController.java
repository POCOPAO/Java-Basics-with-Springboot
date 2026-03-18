package com.bpi.java.training.dashboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

@PreAuthorize("hasRole('MANAGER') or #username == authentication?.name")
	@GetMapping("/profiles/{username}")
	public String getProfileByUsername(@PathVariable String username, Authentication authentication) {
	return "Profile of " + username;
} 
}
