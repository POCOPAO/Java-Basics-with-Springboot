package com.example.demo.service;

import com.example.demo.DAO.Book;
import com.example.demo.DTO.BookDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Book-related CRUD operations.
 * <p>
 * Handles direct interaction with {@link BookRepository} and enforces
 * business rules including deletion constraints based on {@code createdAt}.
 * </p>
 */
@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository repo;

    /**
     * Constructs a {@code BookService} with the given repository.
     *
     * @param repo the book JPA repository
     */
    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all books from the database, sorted by ID ascending.
     *
     * @return list of all books as DTOs
     */
    public List<BookDTO> getAllBooks() {
        log.info("Fetching all books");
        return toDtoList(repo.findAll());
    }

    /**
     * Finds a book by its ID, returning {@code null} if not found.
     *
     * @param id the book ID
     * @return the {@link BookDTO}, or {@code null} if not found
     */
    public BookDTO searchById(Long id) {
        log.debug("Searching book by id={}", id);
        Book book = repo.findById(id).orElse(null);
        return book == null ? null : toDto(book);
    }

    /**
     * Returns the book entity for a given ID, or throws if not found.
     *
     * @param id the book ID
     * @return the {@link Book} entity
     * @throws ResourceNotFoundException if no book with the given ID exists
     */
    public Book getEntityById(Long id) {
        log.debug("Getting book entity by id={}", id);
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book " + id + " not found"));
    }

    /**
     * Searches for books whose title contains the given string (case-insensitive).
     *
     * @param title the partial title to search for
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByTitle(String title) {
        log.debug("Searching books by title containing '{}'", title);
        return toDtoList(repo.findByTitleContainingIgnoreCase(title));
    }

    /**
     * Searches for books by exact author name (case-insensitive).
     *
     * @param author the author name
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByAuthor(String author) {
        log.debug("Searching books by author='{}'", author);
        return toDtoList(repo.findByAuthorIgnoreCase(author));
    }

    /**
     * Searches for books by availability status.
     *
     * @param isAvailable {@code true} for available, {@code false} for loaned
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByAvailability(Boolean isAvailable) {
        log.debug("Searching books by availability={}", isAvailable);
        return toDtoList(repo.findByAvailable(isAvailable));
    }

    /**
     * Searches for books by author and availability.
     *
     * @param author      the author name
     * @param isAvailable the availability flag
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByAuthorAndAvailability(String author, Boolean isAvailable) {
        log.debug("Searching books by author='{}' and availability={}", author, isAvailable);
        return toDtoList(repo.findByAuthorIgnoreCaseAndAvailable(author, isAvailable));
    }

    /**
     * Searches for books by title, author and availability.
     *
     * @param title       partial title
     * @param author      author name
     * @param isAvailable availability flag
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByTitleAuthorAndAvailability(String title, String author, Boolean isAvailable) {
        log.debug("Searching books by title='{}', author='{}', availability={}", title, author, isAvailable);
        return toDtoList(repo.findByTitleContainingIgnoreCaseAndAuthorIgnoreCaseAndAvailable(title, author, isAvailable));
    }

    /**
     * Searches for books by exact title and author match (case-insensitive).
     *
     * @param title  the exact title
     * @param author the exact author
     * @return matching books as DTOs
     */
    public List<BookDTO> searchByTitleAuthor(String title, String author) {
        log.debug("Searching books by title='{}' and author='{}'", title, author);
        return toDtoList(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase(title, author));
    }

    /**
     * Creates and persists a new Book with the given title and author.
     * The book is created as available, with {@code createdAt} set to today.
     *
     * @param title  the book title
     * @param author the book author
     * @return the saved book as a DTO
     */
    public BookDTO addBook(String title, String author) {
        log.info("Adding new book: title='{}', author='{}'", title, author);
        Book newBook = new Book(title.trim(), author.trim(), Boolean.TRUE);
        repo.save(newBook);
        log.info("Book saved with id={}, createdAt={}", newBook.getId(), newBook.getCreatedAt());
        return toDto(newBook);
    }

    /**
     * Updates the title and author of an existing book.
     * <p>
     * Throws {@link BusinessException} if the book is currently on loan.
     * </p>
     *
     * @param id     the ID of the book to update
     * @param title  the new title
     * @param author the new author
     * @return the updated book as a DTO
     * @throws BusinessException         if the book is currently loaned out
     * @throws ResourceNotFoundException if no book with the given ID exists
     */
    public BookDTO updateBook(Long id, String title, String author) {
        log.info("Updating book id={} with title='{}', author='{}'", id, title, author);
        Book updateBook = getEntityById(id);

        if (Boolean.FALSE.equals(updateBook.getAvailable())) {
            log.warn("Cannot update book id={}: currently on loan", id);
            throw new BusinessException("Book " + id + " cannot be edited while it is currently loaned");
        }

        updateBook.setTitle(title.trim());
        updateBook.setAuthor(author.trim());
        repo.save(updateBook);
        log.info("Book id={} updated successfully", id);
        return toDto(updateBook);
    }

    /**
     * Updates the availability (loan status) of a book.
     *
     * @param id          the book ID
     * @param isAvailable the new availability value
     * @return the updated book as a DTO
     */
    public BookDTO updateBookLoan(Long id, Boolean isAvailable) {
        log.debug("Updating loan status for book id={} to available={}", id, isAvailable);
        Book updateBook = getEntityById(id);
        updateBook.setAvailable(isAvailable);
        repo.save(updateBook);
        return toDto(updateBook);
    }

    /**
     * Deletes all books matching the given title and author (case-insensitive).
     * <p>
     * Enforces two deletion rules for each matched book:
     * <ul>
     *   <li>The book must have been created <strong>at least 1 week ago</strong>.</li>
     *   <li>The book must have been created <strong>no more than 1 year ago</strong>.</li>
     * </ul>
     * If either rule is violated for any matching book, a {@link BusinessException} is thrown
     * and no books are deleted.
     * </p>
     *
     * @param title  the exact title to match (case-insensitive)
     * @param author the exact author to match (case-insensitive)
     * @return the number of deleted records
     * @throws ResourceNotFoundException if no matching books are found
     * @throws BusinessException         if any book violates the deletion age rules
     */
    @Transactional
    public long deleteByTitleAndAuthor(String title, String author) {
        log.info("Attempting to delete books with title='{}' and author='{}'", title, author);

        List<Book> books = repo.findByTitleIgnoreCaseAndAuthorIgnoreCase(title, author);

        if (books.isEmpty()) {
            log.warn("No books found with title='{}' and author='{}'", title, author);
            throw new ResourceNotFoundException(
                    "No book found with title '" + title + "' and author '" + author + "'");
        }

        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusWeeks(1);
        LocalDate oneYearAgo = today.minusYears(1);

        for (Book book : books) {
            LocalDate created = book.getCreatedAt();
            log.debug("Checking deletion rules for book id={}, createdAt={}", book.getId(), created);

            if (created == null || created.isAfter(oneWeekAgo)) {
                log.warn("Book id={} rejected for deletion: created less than 1 week ago ({})", book.getId(), created);
                throw new BusinessException(
                        "Book '" + book.getTitle() + "' cannot be deleted: it was created less than 1 week ago.");
            }

            if (created.isBefore(oneYearAgo)) {
                log.warn("Book id={} rejected for deletion: created more than 1 year ago ({})", book.getId(), created);
                throw new BusinessException(
                        "Book '" + book.getTitle() + "' cannot be deleted: it is older than 1 year.");
            }
        }

        long deleted = repo.deleteByTitleIgnoreCaseAndAuthorIgnoreCase(title, author);
        log.info("Deleted {} book(s) with title='{}' and author='{}'", deleted, title, author);
        return deleted;
    }

    // ---- Mapping helpers ----

    /**
     * Converts a {@link Book} entity to a {@link BookDTO}.
     *
     * @param b the book entity
     * @return the corresponding DTO
     */
    public BookDTO toDto(Book b) {
        BookDTO dto = new BookDTO();
        dto.setId(b.getId());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setIsAvailable(b.getAvailable());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }

    /**
     * Converts a {@link BookDTO} to a {@link Book} entity.
     *
     * @param b the DTO
     * @return the corresponding entity
     */
    public Book toEntity(BookDTO b) {
        Book entity = new Book();
        entity.setId(b.getId());
        entity.setTitle(b.getTitle());
        entity.setAuthor(b.getAuthor());
        entity.setAvailable(b.getIsAvailable());
        entity.setCreatedAt(b.getCreatedAt());
        return entity;
    }

    /**
     * Converts a list of {@link Book} entities to a sorted list of {@link BookDTO}s.
     *
     * @param books the list of entities
     * @return sorted DTO list
     */
    private List<BookDTO> toDtoList(List<Book> books) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
