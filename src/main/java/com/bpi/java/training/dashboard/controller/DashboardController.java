package com.bpi.java.training.dashboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

@PreAuthorize("hasRole('USER')")	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "User dashboard!";
	}
}
