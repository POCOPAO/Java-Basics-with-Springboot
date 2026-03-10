package com.bpi.java.training.book.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpi.java.training.book.model.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	// static list of 3 books
	private final List<Book> books = Arrays.asList(
			new Book(1, "El filibusterismo", "Jose Rizal"), 
			new Book(2, "Noli Me Tangere", "Jose Rizal"), 
			new Book(3, "Florante at Laura", "Francisco Balagtas"));
	
	// GET /api/books
	@GetMapping
	public List<Book> getAllBooks(){
		return books;
	}
	
	// GET /api/books/{id}
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable int id) {
		return books.stream().filter(book -> book.getId()==id).findFirst().orElse(null);
	}
}
