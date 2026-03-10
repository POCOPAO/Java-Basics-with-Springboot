package com.bpi.java.training.book.service;

import org.springframework.stereotype.Service;

@Service
public class LoggerService {
		
	public void log(String msg) {
		System.out.println("LOG: " + msg);
	}
	
}
