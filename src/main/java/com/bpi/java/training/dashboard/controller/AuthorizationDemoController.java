package com.bpi.java.training.dashboard.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

	public String profilePage(Principal principal) {
		return "Hello, " + principal.getName();

	}
	
	@GetMapping("/profile/{username}")
	@PreAuthorize("hasRole('USER')")
	public String profilePage(@PathVariable(name = "username") String username) {
		return "Profile of " + username;
	}
	
	@GetMapping("/admin")
	public String adminPage() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated()) {
		String username = auth.getName();
		
		System.out.print("Username: " + username);
		
		Object principal = auth.getPrincipal();
		
		
			Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
		}

		
		return "Admin";
	}
}
