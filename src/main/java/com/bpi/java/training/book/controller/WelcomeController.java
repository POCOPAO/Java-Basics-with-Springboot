package com.bpi.java.training.book.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeController {

	@Value("${welcome.message}")
	private String welcomeMessage;
	
	@GetMapping("/welcome")
	public String getWelcomeMessage() {
		return welcomeMessage;
	}
}
