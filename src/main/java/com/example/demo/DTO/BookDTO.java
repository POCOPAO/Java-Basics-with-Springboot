package com.example.demo.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;


public class BookDTO {
	
	private Long id;
	
	@NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    /** Ensure JSON field name is exactly "isAvailable" */
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    public BookDTO() {}

    public BookDTO(long id, String title, String author, Boolean isAvailable) {
    	this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    // --- Getters / Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    
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

