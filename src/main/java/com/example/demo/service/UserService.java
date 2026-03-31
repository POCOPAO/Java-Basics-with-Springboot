package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.DAO.User;
import com.example.demo.DTO.UserDTO;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository repo;
	
	public UserService(UserRepository repo) {
		this.repo = repo;
	}
	
	public UserDTO searchById(Long id) {
		User user = repo.findById(id).orElse(null);
		return user == null ? null : toDto(user);
	}

	public UserDTO searchByName(String name) {
		User user = repo.findByUserName(name);
		return user == null ? null : toDto(user);
	}
	
	public Optional<User> findByNameEntity(String name) {
		return Optional.ofNullable(repo.findByUserName(name));
	}
	
	@Transactional
	public User findOrCreateEntityByName(String name) {
		User existing = repo.findByUserName(name);
		if (existing != null) {
			return existing;
		}
		User user = new User();
		user.setName(name);
		return repo.save(user);
	}
	
	private UserDTO toDto(User u) {
		UserDTO dto = new UserDTO();
		dto.setId(u.getId());
		dto.setName(u.getName());
		return dto;
	}
	
	public User toEntity(UserDTO u) {
		User entity = new User();
		entity.setId(u.getId());
		entity.setName(u.getName());
		return entity;
	}
}
