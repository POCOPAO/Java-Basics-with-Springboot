package com.bpi.java.training.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {
	
	@GetMapping("/reports")
	public String reports() {
		return "Manager reports";
	}
}
