package com.bpi.java.training.dashboard.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.bpi.java.training.dashboard.security.CustomAccessDeniedHandler;
import com.bpi.java.training.dashboard.security.CustomAuthEntryPoint;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


	@Autowired
	private CustomAuthEntryPoint authEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		
		.csrf(crsf -> crsf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/**").permitAll()
				.requestMatchers("/api/users/create","/public","/public/*").permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/profile").hasRole("USER")
				.anyRequest().authenticated()
				)
		.exceptionHandling(ex -> ex
				.authenticationEntryPoint(authEntryPoint)
				.accessDeniedHandler(accessDeniedHandler))
		.httpBasic(Customizer.withDefaults())
				;
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	
}
