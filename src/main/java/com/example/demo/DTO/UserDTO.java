package com.example.demo.DTO;

public class UserDTO {
	private Long id;
	private String userName;
	
	public UserDTO() {
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return this.userName;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.userName = name;
	}
}
