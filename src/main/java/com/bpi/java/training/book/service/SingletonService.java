package com.bpi.java.training.book.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

/**
  * Default scope in Spring is singleton
 */
@Service
public class SingletonService {

	private final String id = UUID.randomUUID().toString();
	
	public void print() {
		System.out.println("[SingletonService] id=" + id + " | hashCode= " + System.identityHashCode(this));
	}
	
}
