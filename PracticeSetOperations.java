package com.java.list;

import java.util.HashSet;
import java.util.Scanner;

public class PracticeSetOperations {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<String> products = new HashSet<>();
		products.add("Laptop");
		products.add("Mouse");
		products.add("Keyboard");
		products.add("Monitor");
		products.add("Printer");
		
		Scanner scanner = new Scanner(System.in);
		int option;
		
	do {
		ShowMenu();
		option = scanner.nextInt();
		scanner.nextLine();
		
		switch (option) {
			case 1:
				System.out.print("Enter product name to search: ");
				String searchProduct = scanner.nextLine();
				
				if (products.contains(searchProduct)) {
					System.out.println("Product found: " + searchProduct);
				} else {
					System.out.println("Product not found");
				}
				break;
				
			case 2:
				System.out.print("Enter product name to add: ");
				String addProduct = scanner.nextLine();
				
				products.add(addProduct);
				System.out.println("Product added: " + addProduct);
				break;
			
			case 3:
				System.out.println("\nAll Products");
				for (String product: products) {
					System.out.println(product);
				}
				
				System.out.println("\nTotal unique products: " + products.size());
				break;
			case 4:
				System.out.println("Exiting...");
		}} while (option != 5);
		
	
	scanner.close();
	}
public static void ShowMenu(){
		System.out.println("\nSelect an Option:");
		System.out.println("1. Search a product");
		System.out.println("2. Add a product");
		System.out.println("3. Print all products and count");
		System.out.println("4. Exit");
		System.out.print("> ");
		
		
	}
}
