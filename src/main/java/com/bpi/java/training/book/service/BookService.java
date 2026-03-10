package com.bpi.java.training.book.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Creates two instances of each service class and calls print
 * Note:
 * - For SingletonService, both references resolve to the same bean.
 * - For PrototypeService, each getObject() yields a new instance. 
 */
@Service
public class BookService {
	
	//Inject a *single* singleton bean
	private final SingletonService singletonA;
	private final SingletonService singletonB;
	
	// For prototype beans, inject a provider to request fresh instances
	
	private final ObjectProvider<PrototypeService> prototypeProvider;
	
	public BookService(SingletonService singletonA, SingletonService singletonB, ObjectProvider<PrototypeService> prototypeProvider) {
		this.singletonA = singletonA;
		this.singletonB = singletonB;
		this.prototypeProvider = prototypeProvider;
		
	}
	
	public void runBook() {
		System.out.println("--Singleton beans (should be SAME instance) --");
		singletonA.print();
		singletonB.print();
		System.out.println("--Prototype beans (should be DIFFERENT instance) --");
		PrototypeService proto1 = prototypeProvider.getObject();
		PrototypeService proto2 = prototypeProvider.getObject();
		proto1.print();
		proto2.print();
	}
	@Autowired
	private LoggerService loggerService;
	
	
	public void processBook() {
		loggerService.log("Processing book using Field Injection...");
	}
	
}
