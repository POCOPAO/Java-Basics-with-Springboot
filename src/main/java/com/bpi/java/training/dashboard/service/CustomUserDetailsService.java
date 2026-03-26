package com.bpi.java.training.dashboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bpi.java.training.dashboard.model.User;
import com.bpi.java.training.dashboard.repository.RoleRepository;
import com.bpi.java.training.dashboard.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username)
			.orElseThrow(()->
				new UsernameNotFoundException("User not found: "));
		
		List<String> roles = roleRepository.findRoleByUserId(user.getId());
		
		List<SimpleGrantedAuthority> authorities = roles.stream()
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				user.isEnabled(),
				true, true, true,
				authorities
				);
	}
	
}