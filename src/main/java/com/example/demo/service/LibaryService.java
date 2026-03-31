package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

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

@Service
public class LibaryService {

    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    public LibaryService(BookService bookService, UserService userService, LoanService loanService) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
    }

    @Transactional(readOnly = true)
    public List<BookDTOMasked> getBooks(String title, String author, Boolean isAvailable) {
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

        return books.stream().map(this::toMaskedDto).collect(Collectors.toList());
    }

    public BookDTO addBook(String title, String author) {
        return bookService.addBook(title, author);
    }

    public long deleteBookByTitleAndAuthor(String title, String author) {
        List<BookDTO> books = bookService.searchByTitleAuthor(title, author);
        List<Book> bookEntities = books.stream().map(bookService::toEntity).collect(Collectors.toList());
        for (Book book : bookEntities) {
            loanService.deleteByBookId(book.getId());
        }
        return bookService.deleteByTitleAndAuthor(title, author);
    }

    public BookDTO updateBook(Long id, String title, String author) {
        BookDTO existing = bookService.searchById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Book " + id + " not found");
        }
        return bookService.updateBook(id, title, author);
    }

    @Transactional(rollbackFor = Exception.class)
    public LoanDTO borrowBook(Long bookId) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        Book book = bookService.getEntityById(bookId);

        User user = userService.findOrCreateEntityByName(currentUsername);

        if (loanService.loanByUser(user.getId()) >= 5) {
            throw new BusinessException("User has reached the loan limit");
        }
        if (Boolean.FALSE.equals(book.getAvailable())) {
            throw new BusinessException("Book is already loaned");
        }

        bookService.updateBookLoan(bookId, false);
        book.setAvailable(false);

        Loan loan = new Loan();
        loan.setLoan(user, book);

        return loanService.addLoan(loan);
    }

    @Transactional(rollbackFor = Exception.class)
    public long returnBook(Long bookId) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        User currentUser = userService.findByNameEntity(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User " + currentUsername + " not found"));

        BookDTO bookDto = bookService.searchById(bookId);
        if (bookDto == null) {
            throw new ResourceNotFoundException("Book " + bookId + " not found");
        }

        if (!loanService.userHasLoanForBook(bookId, currentUser.getId())) {
            throw new BusinessException("Current user does not have this book on loan");
        }

        long deleted = loanService.deleteByBookIdAndUserId(bookId, currentUser.getId());
        if (deleted == 0) {
            throw new BusinessException("No active loan found for the current user and book");
        }

        bookService.updateBookLoan(bookId, true);
        return deleted;
    }

    private BookDTOMasked toMaskedDto(BookDTO b) {
        BookDTOMasked dtoM = new BookDTOMasked();
        dtoM.setTitle(b.getTitle());
        dtoM.setAuthor(b.getAuthor());
        dtoM.setIsAvailable(b.getIsAvailable());
        return dtoM;
    }
}
