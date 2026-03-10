package com.bpi.java.training.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

	private LoggerService loggerService;
	
	//Setter injection
	@Autowired
	public void setLoggerService(LoggerService loggerService) {
		this.loggerService = loggerService;
	}
	
	public void processBook() {
		loggerService.log("Processing book using Setter Injection...");
	}
	
}
