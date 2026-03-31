package com.example.demo.service;

import com.example.demo.DAO.Book;
import com.example.demo.DTO.BookDTO;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

	private final BookRepository repo;

	public BookService(BookRepository repo) {
		this.repo = repo;
	}

	public List<BookDTO> getAllBooks() {
		return toDtoList(repo.findAll());
	}

	public BookDTO searchById(Long id) {
		Book book = repo.findById(id).orElse(null);
		return book == null ? null : toDto(book);
	}

	public Book getEntityById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book " + id + " not found"));
	}

	public List<BookDTO> searchByTitle(String title) {
		return toDtoList(repo.findByTitleContainingIgnoreCase(title));
	}

	public List<BookDTO> searchByAuthor(String author) {
		return toDtoList(repo.findByAuthorIgnoreCase(author));
	}

	public List<BookDTO> searchByAvailability(Boolean isAvailable) {
		return toDtoList(repo.findByAvailable(isAvailable));
	}

	public List<BookDTO> searchByAuthorAndAvailability(String author, Boolean isAvailable) {
		return toDtoList(repo.findByAuthorIgnoreCaseAndAvailable(author, isAvailable));
	}

	public List<BookDTO> searchByTitleAuthorAndAvailability(String title, String author, Boolean isAvailable) {
		return toDtoList(repo.findByTitleContainingIgnoreCaseAndAuthorIgnoreCaseAndAvailable(title, author, isAvailable));
	}
	
	public List<BookDTO> searchByTitleAuthor(String title, String author) {
		return toDtoList(repo.findByTitleIgnoreCaseAndAuthorIgnoreCase(title, author));
	}

	public BookDTO addBook(String title, String author) {
		Book newBook = new Book(title.trim(), author.trim(), Boolean.TRUE);
		repo.save(newBook);
		return toDto(newBook);
	}

	public BookDTO updateBook(Long id, String title, String author) {
		Book updateBook = getEntityById(id);

		if (Boolean.FALSE.equals(updateBook.getAvailable())) {
			throw new BusinessException("Book " + id + " cannot be edited while it is currently loaned");
		}

		updateBook.setTitle(title.trim());
		updateBook.setAuthor(author.trim());
		repo.save(updateBook);
		return toDto(updateBook);
	}
	
	public BookDTO updateBookLoan(Long id, Boolean isAvailable) {
		Book updateBook = getEntityById(id);
		updateBook.setAvailable(isAvailable);
		repo.save(updateBook);
		return toDto(updateBook);
	}

	@Transactional
	public long deleteByTitleAndAuthor(String title, String author) {
		long deleted = repo.deleteByTitleIgnoreCaseAndAuthorIgnoreCase(title, author);
		if (deleted == 0) {
			throw new ResourceNotFoundException("No book found with title '" + title + "' and author '" + author + "'");
		}
		return deleted;
	}

	private BookDTO toDto(Book b) {
		BookDTO dto = new BookDTO();
		dto.setId(b.getId());
		dto.setTitle(b.getTitle());
		dto.setAuthor(b.getAuthor());
		dto.setIsAvailable(b.getAvailable());
		return dto;
	}
	
	public Book toEntity(BookDTO b) {
		Book entity = new Book();
		entity.setId(b.getId());
		entity.setTitle(b.getTitle());
		entity.setAuthor(b.getAuthor());
		entity.setAvailable(b.getIsAvailable());
		return entity;
	}
	
	private List<BookDTO> toDtoList(List<Book> b) {
		return b.stream().sorted(Comparator.comparing(Book::getId)).map(this::toDto).collect(Collectors.toList());
	}
}
