package com.bpi.java.training.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/public", "/login").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/dashboard/**")
				.hasRole("USER").anyRequest().authenticated()).httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults());
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.withUsername("pdnaranjo").password("{noop}pdnaranjo").roles("USER").build();
		UserDetails admin1 = User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build();
		
		return new InMemoryUserDetailsManager(user1 ,admin1 );
	}
	
}
