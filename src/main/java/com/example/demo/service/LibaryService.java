package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DAO.Book;
import com.example.demo.DAO.Loan;
import com.example.demo.DAO.User;
import com.example.demo.DTO.BookDTO;
import com.example.demo.DTO.BookDTOMasked;
import com.example.demo.DTO.LoanDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.security.SecurityUtils;

/**
 * Orchestration service for the Library/Book Shop system.
 * <p>
 * Coordinates {@link BookService}, {@link UserService}, and {@link LoanService}
 * to implement higher-level business workflows such as borrowing and returning books,
 * filtering catalog results, and enforcing delete constraints.
 * </p>
 */
@Service
public class LibaryService {

    private static final Logger log = LoggerFactory.getLogger(LibaryService.class);

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    /**
     * Constructs a {@code LibaryService} with required dependencies.
     *
     * @param bookService the book service
     * @param userService the user service
     * @param loanService the loan service
     */
    public LibaryService(BookService bookService, UserService userService, LoanService loanService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
    }

    /**
     * Returns a filtered list of books based on optional query parameters.
     * Results are masked (no ID or createdAt) for non-admin display.
     *
     * @param title       optional title filter (partial, case-insensitive)
     * @param author      optional author filter (case-insensitive)
     * @param isAvailable optional availability filter
     * @return list of masked book DTOs
     */
    @Transactional(readOnly = true)
    public List<BookDTOMasked> getBooks(String title, String author, Boolean isAvailable) {
        log.info("Getting books with filters: title='{}', author='{}', isAvailable={}", title, author, isAvailable);

        String t = (title != null && !title.isBlank()) ? title.trim() : null;
        String a = (author != null && !author.isBlank()) ? author.trim() : null;
        Boolean avail = isAvailable;

        List<BookDTO> books;

        if (t == null && a == null && avail == null) {
            books = bookService.getAllBooks();
        } else if (t != null && a == null && avail == null) {
            books = bookService.searchByTitle(t);
        } else if (t == null && a != null && avail == null) {
            books = bookService.searchByAuthor(a);
        } else if (t == null && a == null && avail != null) {
            books = bookService.searchByAvailability(avail);
        } else if (t == null && a != null && avail != null) {
            books = bookService.searchByAuthorAndAvailability(a, avail);
        } else if (t != null && a != null && avail != null) {
            books = bookService.searchByTitleAuthorAndAvailability(t, a, avail);
        } else {
            books = List.of();
        }

        log.info("Found {} book(s) matching filters", books.size());
        return books.stream().map(this::toMaskedDto).collect(Collectors.toList());
    }

    /**
     * Adds a new book to the system.
     *
     * @param title  the book title
     * @param author the book author
     * @return the created book as a full DTO
     */
    public BookDTO addBook(String title, String author) {
        log.info("Adding book: title='{}', author='{}'", title, author);
        return bookService.addBook(title, author);
    }

    /**
     * Deletes all books matching the given title and author.
     * Loan records for matching books are cleaned up first.
     * Enforces age-based deletion rules (see {@link BookService#deleteByTitleAndAuthor}).
     *
     * @param title  the exact title (case-insensitive)
     * @param author the exact author (case-insensitive)
     * @return the number of deleted books
     */
    public long deleteBookByTitleAndAuthor(String title, String author) {
        log.info("Deleting books: title='{}', author='{}'", title, author);
        List<BookDTO> books = bookService.searchByTitleAuthor(title, author);
        List<Book> bookEntities = books.stream().map(bookService::toEntity).collect(Collectors.toList());

        for (Book book : bookEntities) {
            log.debug("Removing loans for book id={}", book.getId());
            loanService.deleteByBookId(book.getId());
        }

        return bookService.deleteByTitleAndAuthor(title, author);
    }

    /**
     * Updates the title and/or author of an existing book by ID.
     *
     * @param id     the book ID
     * @param title  the new title
     * @param author the new author
     * @return the updated book as a full DTO
     * @throws ResourceNotFoundException if no book with the given ID exists
     */
    public BookDTO updateBook(Long id, String title, String author) {
        log.info("Updating book id={}", id);
        BookDTO existing = bookService.searchById(id);
        if (existing == null) {
            log.warn("Book id={} not found for update", id);
            throw new ResourceNotFoundException("Book " + id + " not found");
        }
        return bookService.updateBook(id, title, author);
    }

    /**
     * Allows the currently authenticated user to borrow a book.
     * <p>
     * Rules enforced:
     * <ul>
     *   <li>User cannot have more than 5 active loans.</li>
     *   <li>Book must be currently available (not already loaned).</li>
     * </ul>
     * </p>
     *
     * @param bookId the ID of the book to borrow
     * @return the created loan as a DTO
     * @throws BusinessException         if loan limit is reached or book is unavailable
     * @throws ResourceNotFoundException if the book does not exist
     */
    @Transactional(rollbackFor = Exception.class)
    public LoanDTO borrowBook(Long bookId) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        log.info("User '{}' attempting to borrow book id={}", currentUsername, bookId);

        Book book = bookService.getEntityById(bookId);
        User user = userService.findOrCreateEntityByName(currentUsername);

        if (loanService.loanByUser(user.getId()) >= 5) {
            log.warn("User '{}' has reached the loan limit", currentUsername);
            throw new BusinessException("User has reached the loan limit");
        }
        if (Boolean.FALSE.equals(book.getAvailable())) {
            log.warn("Book id={} is already loaned", bookId);
            throw new BusinessException("Book is already loaned");
        }

        bookService.updateBookLoan(bookId, false);
        book.setAvailable(false);

        Loan loan = new Loan();
        loan.setLoan(user, book);

        LoanDTO result = loanService.addLoan(loan);
        log.info("Book id={} borrowed by user '{}', loan id={}", bookId, currentUsername, result.getId());
        return result;
    }

    /**
     * Allows the currently authenticated user to return a borrowed book.
     *
     * @param bookId the ID of the book to return
     * @return the number of loan records deleted (should be 1)
     * @throws ResourceNotFoundException if the book or user does not exist
     * @throws BusinessException         if the user does not have this book on loan
     */
    @Transactional(rollbackFor = Exception.class)
    public long returnBook(Long bookId) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        log.info("User '{}' attempting to return book id={}", currentUsername, bookId);

        User currentUser = userService.findByNameEntity(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User " + currentUsername + " not found"));

        BookDTO bookDto = bookService.searchById(bookId);
        if (bookDto == null) {
            log.warn("Book id={} not found for return", bookId);
            throw new ResourceNotFoundException("Book " + bookId + " not found");
        }

        if (!loanService.userHasLoanForBook(bookId, currentUser.getId())) {
            log.warn("User '{}' does not have book id={} on loan", currentUsername, bookId);
            throw new BusinessException("Current user does not have this book on loan");
        }

        long deleted = loanService.deleteByBookIdAndUserId(bookId, currentUser.getId());
        if (deleted == 0) {
            throw new BusinessException("No active loan found for the current user and book");
        }

        bookService.updateBookLoan(bookId, true);
        log.info("Book id={} returned by user '{}'", bookId, currentUsername);
        return deleted;
    }

    /**
     * Converts a full {@link BookDTO} to a masked version (hides ID and createdAt).
     *
     * @param b the full DTO
     * @return the masked DTO
     */
    private BookDTOMasked toMaskedDto(BookDTO b) {
        BookDTOMasked dtoM = new BookDTOMasked();
        dtoM.setTitle(b.getTitle());
        dtoM.setAuthor(b.getAuthor());
        dtoM.setIsAvailable(b.getIsAvailable());
        return dtoM;
    }
}
