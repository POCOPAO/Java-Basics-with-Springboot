package com.example.demo.controller;

import java.util.List;

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

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final LibaryService libaryService;

    public BookController(LibaryService libaryService) {
        this.libaryService = libaryService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<BookDTOMasked>> getBooks(@RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String author,
                                                        @RequestParam(required = false) Boolean isAvailable) {
        return ResponseEntity.ok(libaryService.getBooks(title, author, isAvailable));
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookDTO> addBook(@RequestParam String title,
                                           @RequestParam String author) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libaryService.addBook(title, author));
    }

    @DeleteMapping("/delBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> delBook(@RequestParam String title,
                                          @RequestParam String author) {
        long count = libaryService.deleteBookByTitleAndAuthor(title, author);
        return ResponseEntity.ok("Deleted " + count + " record(s).");
    }

    @PatchMapping("/updateBook")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookDTO> updateBook(@RequestParam Long id,
                                              @RequestParam String title,
                                              @RequestParam String author) {
        return ResponseEntity.ok(libaryService.updateBook(id, title, author));
    }

    @PostMapping("/borrowBook")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanDTO> borrowBook(@RequestParam Long bookId) {
        LoanDTO loan = libaryService.borrowBook(bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PostMapping("/returnBook")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> returnBook(@RequestParam Long bookId) {
        long deleted = libaryService.returnBook(bookId);
        return ResponseEntity.ok(deleted);
    }
}
