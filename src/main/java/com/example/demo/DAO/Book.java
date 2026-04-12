package com.example.demo.DAO;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * Entity representing a Book in the Book Shop system.
 * <p>
 * Maps to the {@code books} table in PostgreSQL. Tracks title, author,
 * availability status, and the date the book was created (added) in the system.
 * </p>
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The book's title. Must not be blank; max 50 characters. */
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    /** The book's author. Must not be blank; max 50 characters. */
    @Column(name = "author", nullable = false, length = 50)
    private String author;

    /** Whether the book is currently available for borrowing. */
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    /**
     * The date this book record was created (i.e., added to the system).
     * Set automatically via {@link PrePersist} if not provided.
     * Used to enforce deletion rules:
     * <ul>
     *   <li>Cannot delete if created less than 1 week ago.</li>
     *   <li>Cannot delete if created more than 1 year ago.</li>
     * </ul>
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    /** JPA requires a no-arg constructor. */
    public Book() {}

    /**
     * Constructs a new Book with the given title, author, and availability.
     *
     * @param title     the book title
     * @param author    the book author
     * @param available whether the book is available for borrowing
     */
    public Book(String title, String author, Boolean available) {
        this.title = title;
        this.author = author;
        this.available = available;
    }

    /**
     * Automatically sets {@code createdAt} to today if not already set
     * before the entity is first persisted.
     */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDate.now();
        }
    }

    // --- Getters / Setters ---

    /** @return the database-generated primary key */
    public Long getId() { return id; }

    /** @param id the primary key to set */
    public void setId(Long id) { this.id = id; }

    /** @return the book title */
    public String getTitle() { return title; }

    /** @param title the book title to set */
    public void setTitle(String title) { this.title = title; }

    /** @return the book author */
    public String getAuthor() { return author; }

    /** @param author the book author to set */
    public void setAuthor(String author) { this.author = author; }

    /** @return {@code true} if the book is available for borrowing */
    public Boolean getAvailable() { return available; }

    /** @param available the availability flag to set */
    public void setAvailable(Boolean available) { this.available = available; }

    /** @return the date this book was added to the system */
    public LocalDate getCreatedAt() { return createdAt; }

    /** @param createdAt the creation date to set */
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
