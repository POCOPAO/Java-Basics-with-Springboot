package com.bpi.java.training.dashboard;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bpi.java.training.dashboard.model.Role;
import com.bpi.java.training.dashboard.model.User;
import com.bpi.java.training.dashboard.repository.RoleRepository;
import com.bpi.java.training.dashboard.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public void run(String... args) {
		
		Role userRole = roleRepository.findByName("ROLE_USER")
				.orElseGet(()-> roleRepository.save(new Role("ROLE_USER")));
		
		Role managerRole = roleRepository.findByName("ROLE_MANAGER")
				.orElseGet(()-> roleRepository.save(new Role("ROLE_MANAGER")));
		
		if (userRepository.findByUsername("dev_1").isEmpty()) {
			userRepository.save(new User("dev_1", passwordEncoder.encode("dev1pass"),
					Set.of(userRole)));
		
		if (userRepository.findByUsername("dev_2").isEmpty()) {
			userRepository.save(new User("dev_2", passwordEncoder.encode("dev2pass"),
					Set.of(userRole)));
	
		if (userRepository.findByUsername("mgr_1").isEmpty()) {
			userRepository.save(new User("mgr_1", passwordEncoder.encode("mgr1pass"),
					Set.of(managerRole)));
				
			
		}
	}
	
}
	}}
