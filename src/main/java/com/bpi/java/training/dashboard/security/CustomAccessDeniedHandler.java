package com.bpi.java.training.dashboard.security;

import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
@Override
public void handle(HttpServletRequest req, HttpServletResponse res,
	                   AccessDeniedException ex) throws IOException {
res.setStatus(HttpServletResponse.SC_FORBIDDEN);
	      res.setContentType("application/json");
	      res.getWriter().write("{\"error\":\"Forbidden\",\"message\":\"" + ex.getMessage() + "\"}");
	 }
}

