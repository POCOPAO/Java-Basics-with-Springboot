package com.bpi.java.training.book.dao;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "author", nullable = false, length = 50)
    private String author;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    public Book() {}

    public Book(String title, String author, Boolean available) {
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    /** Business-friendly naming */
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }

}