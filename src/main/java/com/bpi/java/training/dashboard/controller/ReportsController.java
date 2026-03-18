package com.bpi.java.training.dashboard.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {
	
@PreAuthorize("hasRole('MANAGER')")	
	@GetMapping("/reports")
	public String reports() {
		return "Manager reports";
	}
}
