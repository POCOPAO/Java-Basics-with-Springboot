package com.bpi.java.training.book.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bpi.java.training.book.model.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	// mutable list of books(simulating an in-memory DB
	private final List<Book> books = new ArrayList<>(Arrays.asList(
			new Book(1, "El filibusterismo", "Jose Rizal"), 
			new Book(2, "Noli Me Tangere", "Jose Rizal"), 
			new Book(3, "Florante at Laura", "Francisco Balagtas")));
	
	//simple sequence generator for new IDs
	private AtomicInteger idSequence = new AtomicInteger(3);
	
	// GET /api/books
	@GetMapping
	@ResponseBody
	public List<Book> getAllBooks(){
		return books;
	}
	
	// GET /api/books/{id}
	@GetMapping("/{id}")
	public Book getBookById(@PathVariable int id) {
		return books.stream().filter(book -> book.getId()==id).findFirst().orElse(null);
	}
	
	//GET /api/books/search?id={id}
	@GetMapping("/search")
	public Book getBookByQuery(@RequestParam int id) {
		return books.stream().filter(book -> book.getId()==id).findFirst().orElse(null);
	}
	
	// === NEW: POST /api/books ===
	@PostMapping public ResponseEntity<Book> createBook(@RequestBody Book incoming){
		//basic validation
		if (incoming.getTitle() == null || incoming.getTitle().isBlank() || incoming.getAuthor() == null || incoming.getAuthor().isBlank()) {
			return ResponseEntity.badRequest().build();
		}
		
		//assign a new id and "save"
		int newId = idSequence.incrementAndGet();
		Book saved = new Book(newId, incoming.getTitle(), incoming.getAuthor());
		books.add(saved);
		
		// build location: /api/books/{id}
		URI location = URI.create("/api/books/" + newId);
		return ResponseEntity.created(location).body(saved);
	}
}
