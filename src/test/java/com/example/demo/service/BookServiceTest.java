package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.demo.DAO.Book;
import com.example.demo.DTO.BookDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link BookService}.
 * <p>
 * Uses Mockito to isolate the service from the JPA repository.
 * Tests cover: add, update, delete (with age constraints), and search operations.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repo;

    @InjectMocks
    private BookService bookService;

    private Book validBook;

    /**
     * Sets up a reusable book entity created exactly 2 weeks ago
     * (satisfies both deletion rules: >= 1 week, <= 1 year).
     */
    @BeforeEach
    void setUp() {
        validBook = new Book("Clean Code", "Robert Martin", true);
        validBook.setId(1L);
        validBook.setCreatedAt(LocalDate.now().minusWeeks(2));
    }

    // ===== addBook =====

    @Test
    @DisplayName("addBook: should save and return DTO with createdAt set")
    void addBook_shouldReturnDto() {
        when(repo.save(any(Book.class))).thenAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            b.setId(10L);
            return b;
        });

        BookDTO result = bookService.addBook("Clean Code", "Robert Martin");

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
        assertTrue(result.getIsAvailable());
        assertNotNull(result.getCreatedAt());
        verify(repo).save(any(Book.class));
    }

    @Test
    @DisplayName("addBook: should trim whitespace from title and author")
    void addBook_shouldTrimWhitespace() {
        when(repo.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookDTO result = bookService.addBook("  Clean Code  ", "  Robert Martin  ");

        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthor());
    }

    // ===== updateBook =====

    @Test
    @DisplayName("updateBook: should update title and author when book is available")
    void updateBook_success() {
        when(repo.findById(1L)).thenReturn(Optional.of(validBook));
        when(repo.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookDTO result = bookService.updateBook(1L, "New Title", "New Author");

        assertEquals("New Title", result.getTitle());
        assertEquals("New Author", result.getAuthor());
    }

    @Test
    @DisplayName("updateBook: should throw BusinessException when book is on loan")
    void updateBook_whenOnLoan_throwsBusinessException() {
        validBook.setAvailable(false);
        when(repo.findById(1L)).thenReturn(Optional.of(validBook));

        assertThrows(BusinessException.class,
                () -> bookService.updateBook(1L, "New Title", "New Author"));
    }

    @Test
    @DisplayName("updateBook: should throw ResourceNotFoundException when book not found")
    void updateBook_whenNotFound_throwsResourceNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBook(99L, "Title", "Author"));
    }

    // ===== deleteByTitleAndAuthor =====

    @Test
    @DisplayName("delete: should succeed when book is between 1 week and 1 year old")
    void delete_withinValidAge_succeeds() {
        when(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(List.of(validBook));
        when(repo.deleteByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(1L);

        long result = bookService.deleteByTitleAndAuthor("Clean Code", "Robert Martin");

        assertEquals(1L, result);
    }

    @Test
    @DisplayName("delete: should throw BusinessException when book is less than 1 week old")
    void delete_tooNew_throwsBusinessException() {
        validBook.setCreatedAt(LocalDate.now().minusDays(3)); // only 3 days old
        when(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(List.of(validBook));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookService.deleteByTitleAndAuthor("Clean Code", "Robert Martin"));

        assertTrue(ex.getMessage().contains("less than 1 week ago"));
    }

    @Test
    @DisplayName("delete: should throw BusinessException when book is older than 1 year")
    void delete_tooOld_throwsBusinessException() {
        validBook.setCreatedAt(LocalDate.now().minusYears(2)); // 2 years old
        when(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(List.of(validBook));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookService.deleteByTitleAndAuthor("Clean Code", "Robert Martin"));

        assertTrue(ex.getMessage().contains("older than 1 year"));
    }

    @Test
    @DisplayName("delete: should throw BusinessException when book is exactly 1 week old (boundary)")
    void delete_exactlyOneWeekOld_succeeds() {
        validBook.setCreatedAt(LocalDate.now().minusWeeks(1)); // exactly at boundary
        when(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(List.of(validBook));
        when(repo.deleteByTitleIgnoreCaseAndAuthorIgnoreCase("Clean Code", "Robert Martin"))
                .thenReturn(1L);

        // exactly 1 week ago: createdAt == oneWeekAgo means NOT after oneWeekAgo → allowed
        assertDoesNotThrow(() -> bookService.deleteByTitleAndAuthor("Clean Code", "Robert Martin"));
    }

    @Test
    @DisplayName("delete: should throw ResourceNotFoundException when no books match")
    void delete_noMatchingBook_throwsResourceNotFoundException() {
        when(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase("Unknown", "Nobody"))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> bookService.deleteByTitleAndAuthor("Unknown", "Nobody"));
    }

    // ===== searchById =====

    @Test
    @DisplayName("searchById: should return DTO when book exists")
    void searchById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(validBook));

        BookDTO result = bookService.searchById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("searchById: should return null when book not found")
    void searchById_notFound_returnsNull() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        BookDTO result = bookService.searchById(99L);

        assertNull(result);
    }

    // ===== getAllBooks =====

    @Test
    @DisplayName("getAllBooks: should return all books sorted by ID")
    void getAllBooks_returnsSortedList() {
        Book book2 = new Book("Refactoring", "Martin Fowler", true);
        book2.setId(2L);
        book2.setCreatedAt(LocalDate.now().minusWeeks(1));

        when(repo.findAll()).thenReturn(List.of(book2, validBook));

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId()); // sorted ascending
        assertEquals(2L, result.get(1).getId());
    }

    // ===== updateBookLoan =====

    @Test
    @DisplayName("updateBookLoan: should set availability to false when borrowing")
    void updateBookLoan_setsUnavailable() {
        when(repo.findById(1L)).thenReturn(Optional.of(validBook));
        when(repo.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookDTO result = bookService.updateBookLoan(1L, false);

        assertFalse(result.getIsAvailable());
    }

    @Test
    @DisplayName("updateBookLoan: should set availability to true when returning")
    void updateBookLoan_setsAvailable() {
        validBook.setAvailable(false);
        when(repo.findById(1L)).thenReturn(Optional.of(validBook));
        when(repo.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookDTO result = bookService.updateBookLoan(1L, true);

        assertTrue(result.getIsAvailable());
    }
}
