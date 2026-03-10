package com.bpi.java.training.book.service;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * A new instance is created every time it is requested from the container
 */
@Service
@Scope(SCOPE_PROTOTYPE)
public class PrototypeService {
	
	private final String id = UUID.randomUUID().toString();
	
	public void print() {
		System.out.println("[PrototypeService] id= " + id + " | hashCode= " + System.identityHashCode(this));
	}
}
