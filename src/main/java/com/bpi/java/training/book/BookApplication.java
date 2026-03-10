package com.bpi.java.training.book;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bpi.java.training.book.service.BookService;


@SpringBootApplication
public class BookApplication implements CommandLineRunner{

	private final BookService bookService;
	
	public BookApplication(BookService bookService) {
		this.bookService = bookService;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BookApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("=== Demonstrating Singleton vs. Prototype ===");
		
		bookService.runBook();
		
		System.out.println("=============================================");
	}
	
}
