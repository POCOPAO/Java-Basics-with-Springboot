package com.example.demo.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for full Book representation.
 * <p>
 * Used for admin-facing responses that include the book ID and creation date.
 * Includes bean validation constraints on required fields.
 * </p>
 */
public class BookDTO {

    /** Database-generated identifier. */
    private Long id;

    /** The book title. Must not be blank; max 50 characters. */
    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title must not exceed 50 characters")
    private String title;

    /** The book author. Must not be blank; max 50 characters. */
    @NotBlank(message = "Author is required")
    @Size(max = 50, message = "Author must not exceed 50 characters")
    private String author;

    /** Availability flag; serialized as {@code "isAvailable"} in JSON. */
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    /** The date this book was added to the system. */
    private LocalDate createdAt;

    /** No-arg constructor required for Jackson deserialization. */
    public BookDTO() {}

    /**
     * Full constructor.
     *
     * @param id          the book ID
     * @param title       the book title
     * @param author      the book author
     * @param isAvailable availability status
     * @param createdAt   date the book was added
     */
    public BookDTO(long id, String title, String author, Boolean isAvailable, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    // --- Getters / Setters ---

    /** @return the book ID */
    public Long getId() { return id; }

    /** @param id the book ID */
    public void setId(Long id) { this.id = id; }

    /** @return the book title */
    public String getTitle() { return title; }

    /** @param title the book title */
    public void setTitle(String title) { this.title = title; }

    /** @return the book author */
    public String getAuthor() { return author; }

    /** @param author the book author */
    public void setAuthor(String author) { this.author = author; }

    /** @return whether the book is available */
    @JsonProperty("isAvailable")
    public Boolean getIsAvailable() { return isAvailable; }

    /** @param isAvailable the availability flag */
    @JsonProperty("isAvailable")
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    /** @return the date the book was created */
    public LocalDate getCreatedAt() { return createdAt; }

    /** @param createdAt the creation date */
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
