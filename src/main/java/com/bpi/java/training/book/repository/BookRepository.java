package com.bpi.java.training.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bpi.java.training.book.dao.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorIgnoreCase(String author);
    List<Book> findByAvailable(Boolean available);
    List<Book> findByAuthorIgnoreCaseAndAvailable(String author, Boolean available);
    List<Book> findByTitleContainingIgnoreCaseAndAuthorIgnoreCaseAndAvailable(String title, String author, Boolean available);
    long deleteByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}