package com.bpi.java.training.dashboard.security;

import java.io.IOException;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
@Override
public void commence(HttpServletRequest req, HttpServletResponse res,
	                     AuthenticationException ex) throws IOException {
	       res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        res.setContentType("application/json");
	        res.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"" + ex.getMessage() + "\"}");
	}
}
