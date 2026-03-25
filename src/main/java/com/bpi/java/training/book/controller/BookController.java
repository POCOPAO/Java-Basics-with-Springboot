package com.bpi.java.training.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bpi.java.training.book.dto.BookDTO;
import com.bpi.java.training.book.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	// GET /api/books
	@GetMapping
	public ResponseEntity<List<BookDTO>> getBooks(@RequestParam(required = false) String title,
			@RequestParam(required = false) String author, @RequestParam(required = false) Boolean isAvailable) {
		List<BookDTO> books = null;

		String t = (title != null && !title.isBlank()) ? title.trim() : null;
		String a = (author != null && !author.isBlank()) ? author.trim() : null;
		Boolean avail = isAvailable;

		if (t == null && a == null && avail == null) {
			books = bookService.getAllBooks();
		}
		if (t != null && a == null && avail == null) {
			books = bookService.searchByTitle(t);
		}
		if (t == null && a != null && avail == null) {
			books = bookService.searchByAuthor(a);
		}
		if (t == null && a == null && avail != null) {
			books = bookService.searchByAvailability(avail);
		}
		if (t == null && a != null && avail != null) {
			books = bookService.searchByAuthorAndAvailability(a, avail);
		}
		if (t != null && a != null && avail != null) {
			books = bookService.searchByTitleAuthorAndAvailability(t, a, avail);
		}
		return ResponseEntity.ok(books);

	}

	@PostMapping("/addBook")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<BookDTO> addBook(@RequestParam(required = true) String title,
			@RequestParam(required = true) String author) {
		BookDTO book = null;
		book = bookService.addBook(title, author);
		return ResponseEntity.ok(book);
	}

	@DeleteMapping("/delBook")
	public ResponseEntity<String> delBook(@RequestParam String title, @RequestParam String author) {

		long count = bookService.deleteByTitleAndAuthor(title, author);
		if (count == 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No book found with given title and author.");
		}
		return ResponseEntity.ok("Deleted " + count + " record(s).");
	}

}