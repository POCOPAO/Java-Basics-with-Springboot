package com.bpi.java.training.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class BookDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    public BookDTO() {}

    public BookDTO(String title, String author, Boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    @JsonProperty("isAvailable")
    public Boolean getIsAvailable() { return isAvailable; }

    @JsonProperty("isAvailable")
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    
}