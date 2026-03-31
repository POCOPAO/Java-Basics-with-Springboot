package com.example.demo.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;


public class BookDTOMasked {
	
	@NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    /** Ensure JSON field name is exactly "isAvailable" */
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    public BookDTOMasked() {}

    public BookDTOMasked(String title, String author, Boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    // --- Getters / Setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    /** Using getIsAvailable()/setIsAvailable with @JsonProperty is explicit and safe */
    @JsonProperty("isAvailable")
    public Boolean getIsAvailable() { return isAvailable; }

    @JsonProperty("isAvailable")
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    
}

