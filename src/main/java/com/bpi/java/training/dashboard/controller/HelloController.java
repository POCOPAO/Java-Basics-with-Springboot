package com.bpi.java.training.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/public/hello")
	public String helloPublic() {
		return "Hello World Public";
	}
	
	@GetMapping("/secured/hello")
	public String hello() {
		return "Hello World Secured";
	}
}
