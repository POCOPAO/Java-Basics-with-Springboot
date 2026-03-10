package com.bpi.java.training.book.service;

import org.springframework.stereotype.Service;

@Service
public class BookService {

	private final LoggerService loggerService;
	
	//Constructor injection
	public BookService(LoggerService loggerService) {
		this.loggerService = loggerService;
	}
	
	public void processBook() {
		loggerService.log("Processing book...");
	}
	
}
