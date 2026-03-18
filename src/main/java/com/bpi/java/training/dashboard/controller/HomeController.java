package com.bpi.java.training.dashboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

@PreAuthorize("hasAnyRole('USER','MANAGER')")
	@GetMapping("/home")
	public String home() {
		return "Welcome to the portal";
	}
}
