package com.example.demo.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Many loans can refer to the same book over time
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    
	public Long getLoanID() {
		return this.id;
	}
    
    public User getUser() {
    	return this.user;
    }
    
    public Book getBook() {
    	return this.book;
    }
    
	public void setLoan(User user, Book book) {
		this.user = user;
		this.book = book;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}

}
