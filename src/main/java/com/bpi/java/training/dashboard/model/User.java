package com.bpi.java.training.dashboard.model;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, name = "username")
	private String username;
	
	@Column(nullable = false, name = "password")
	private String password;
	
	@Column(nullable = false, name = "enabled")
	private boolean enabled = true;
	

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Role> roles;
	
	public User() {}
	
	public User(String username, String password, Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	public Long getId() {
		return id;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<Role> getRoles(){
		return roles;
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
	
}
