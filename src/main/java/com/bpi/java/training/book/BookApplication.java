package com.bpi.java.training.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bpi.java.training.book.service.BookService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BookApplication {

	@Autowired
	private BookService bookService;
	
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	@PostConstruct
	public void run() {
		bookService.processBook();
	}
	
}
