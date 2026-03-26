package com.bpi.java.training.dashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	public Role() {}

	
	
	public Role(User user, String name) {
		this.user = user;
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
