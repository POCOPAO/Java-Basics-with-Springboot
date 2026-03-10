package com.bpi.java.training.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	@Autowired
	private LoggerService loggerService;
	
	
	public void processBook() {
		loggerService.log("Processing book using Field Injection...");
	}
	
}
