package com.bpi.java.training.dashboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.bpi.java.training.dashboard.service.CustomUserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(crsf -> crsf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/users/create").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/profile").hasRole("USER")
				.anyRequest().authenticated()
				)
		.httpBasic(Customizer.withDefaults())
		.authenticationProvider(authenticationProvider());
		
		return http.build();
	}
	
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}


	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	
}
