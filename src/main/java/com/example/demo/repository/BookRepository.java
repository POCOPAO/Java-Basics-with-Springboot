package com.example.demo.repository;

import com.example.demo.DAO.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorIgnoreCase(String author);
    List<Book> findByAvailable(Boolean available);
    List<Book> findByAuthorIgnoreCaseAndAvailable(String author, Boolean available);
    List<Book> findByTitleContainingIgnoreCaseAndAuthorIgnoreCaseAndAvailable(String title, String author, Boolean available);
    List<Book> findByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
    long deleteByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
    

    Book findFirstByTitleIgnoreCaseAndAuthorIgnoreCaseAndAvailableIsTrueOrderByIdAsc(String title, String author);
    Book findFirstByTitleContainingIgnoreCaseAndAvailableIsTrueOrderByIdAsc(String title);
    Optional<Book> findById(Long id);
    
}