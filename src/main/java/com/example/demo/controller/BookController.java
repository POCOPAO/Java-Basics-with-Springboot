package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.BookDTO;
import com.example.demo.DTO.BookDTOMasked;
import com.example.demo.DTO.LoanDTO;
import com.example.demo.service.LibaryService;

/**
 * REST controller for Book Shop API operations.
 * <p>
 * Exposes endpoints for browsing, creating, updating, deleting books,
 * and for borrowing/returning books. Access is controlled via Spring Security roles.
 * </p>
 *
 * <ul>
 *   <li>ADMIN: can add, update, delete books</li>
 *   <li>USER: can browse and borrow/return books</li>
 * </ul>
 *
 * Base URL: {@code /api/books}
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    private final LibaryService libaryService;

    /**
     * Constructs a {@code BookController} with the given library service.
     *
     * @param libaryService the orchestration service
     */
    public BookController(LibaryService libaryService) {
        this.libaryService = libaryService;
    }

    /**
     * Retrieves a filtered list of books.
     * <p>
     * All parameters are optional. If none are provided, all books are returned.
     * Results are masked (no ID or creation date).
     * </p>
     *
     * @param title       optional title filter (partial match, case-insensitive)
     * @param author      optional author filter (exact match, case-insensitive)
     * @param isAvailable optional availability filter
     * @return {@code 200 OK} with the list of matching books
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<BookDTOMasked>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Boolean isAvailable) {
        log.info("GET /api/books called with title='{}', author='{}', isAvailable={}", title, author, isAvailable);
        return ResponseEntity.ok(libaryService.getBooks(title, author, isAvailable));
    }

    /**
     * Creates a new book (ADMIN only).
     * <p>
     * The book is created with {@code isAvailable = true} and {@code createdAt = today}.
     * </p>
     *
     * @param title  the book title (required, max 50 chars)
     * @param author the book author (required, max 50 chars)
     * @return {@code 201 Created} with the new book's full details
     */
    @PostMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDTO> addBook(
            @RequestParam String title,
            @RequestParam String author) {
        log.info("POST /api/books/addBook: title='{}', author='{}'", title, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(libaryService.addBook(title, author));
    }

    /**
     * Deletes book(s) matching the given title and author (ADMIN only).
     * <p>
     * Deletion is subject to age constraints:
     * <ul>
     *   <li>The book must have been created <strong>at least 1 week ago</strong>.</li>
     *   <li>The book must have been created <strong>no more than 1 year ago</strong>.</li>
     * </ul>
     * Returns {@code 400 Bad Request} with a descriptive message if constraints are not met.
     * </p>
     *
     * @param title  the exact title of the book(s) to delete (case-insensitive)
     * @param author the exact author of the book(s) to delete (case-insensitive)
     * @return {@code 200 OK} with a confirmation message indicating how many records were deleted
     */
    @DeleteMapping("/delBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> delBook(
            @RequestParam String title,
            @RequestParam String author) {
        log.info("DELETE /api/books/delBook: title='{}', author='{}'", title, author);
        long count = libaryService.deleteBookByTitleAndAuthor(title, author);
        return ResponseEntity.ok("Deleted " + count + " record(s).");
    }

    /**
     * Updates the title and/or author of an existing book by ID (ADMIN only).
     * <p>
     * The book must not be currently on loan.
     * </p>
     *
     * @param id     the ID of the book to update
     * @param title  the new title
     * @param author the new author
     * @return {@code 200 OK} with the updated book details
     */
    @PatchMapping("/updateBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookDTO> updateBook(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String author) {
        log.info("PATCH /api/books/updateBook: id={}, title='{}', author='{}'", id, title, author);
        return ResponseEntity.ok(libaryService.updateBook(id, title, author));
    }

    /**
     * Allows the authenticated USER to borrow a book.
     * <p>
     * Rules:
     * <ul>
     *   <li>User may not have more than 5 active loans.</li>
     *   <li>Book must be currently available.</li>
     * </ul>
     * </p>
     *
     * @param bookId the ID of the book to borrow
     * @return {@code 201 Created} with the created loan details
     */
    @PostMapping("/borrowBook")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanDTO> borrowBook(@RequestParam Long bookId) {
        log.info("POST /api/books/borrowBook: bookId={}", bookId);
        LoanDTO loan = libaryService.borrowBook(bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    /**
     * Allows the authenticated USER to return a previously borrowed book.
     *
     * @param bookId the ID of the book to return
     * @return {@code 200 OK} with the number of loan records removed
     */
    @PostMapping("/returnBook")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> returnBook(@RequestParam Long bookId) {
        log.info("POST /api/books/returnBook: bookId={}", bookId);
        long deleted = libaryService.returnBook(bookId);
        return ResponseEntity.ok(deleted);
    }
}
