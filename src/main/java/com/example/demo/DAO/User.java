package com.example.demo.DAO;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // matches SERIAL
	private Long id;

	@Column(name = "name", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
	private String userName;

	// One User -> Many Loans (current + historical)
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Loan> loans = new ArrayList<>();

	// ===== Getters & Setters =====
	public Long getId() {
		return id;
	}

	public String getName() {
		return this.userName;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.userName = name;
	}

	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}

}
