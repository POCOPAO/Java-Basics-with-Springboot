package com.bpi.java.training.dashboard.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bpi.java.training.dashboard.dto.UserDTO;
import com.bpi.java.training.dashboard.model.Role;
import com.bpi.java.training.dashboard.model.User;
import com.bpi.java.training.dashboard.repository.RoleRepository;
import com.bpi.java.training.dashboard.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User createUser(UserDTO userDTO) {
		if(userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setEnabled(true);
		
		User savedUser = userRepository.save(user);
		
		if(userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
			for (String roleName : userDTO.getRoles()) {
				Role role = new Role(savedUser, roleName);
				roleRepository.save(role);
			}
		} else {
			Role role = new Role(savedUser, "ROLE_USER");
			roleRepository.save(role);
		}
		
		return savedUser;
	}
}
