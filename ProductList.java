package com.java.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> products = new ArrayList<>();
		
		products.add("Laptop");
		products.add("Mouse");
		products.add("Keyboard");
		products.add("Monitor");
		products.add("Printer");
		
		System.out.println("All Products:");
		for (int i = 0; i < products.size(); i++) {
			System.out.println((i + 1) + ". " + products.get(i));
		}
		
		products.add("Webcam");
		products.remove("Mouse");

		
		System.out.println("\nAfter adding and removing products:");
		for (int i = 0; i < products.size(); i++) {
			System.out.println((i + 1) + ". " + products.get(i));
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter product name to search: ");
		
		String searchProduct = scanner.nextLine();
		
		boolean found = false;
		
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).equals(searchProduct)) {
				found = true;
				break;
			}
		}
		
		if (found) {
			System.out.println("Product found: " + searchProduct);
		} else {
			System.out.println("Product not found.");
		}
		
		/** Using contains()
		if (products.contains(searchProduct)) {
			System.out.println("\nProduct found: " + searchProduct);
		} else {
			System.out.println("\nProduct not found.");
		}
		**/
		scanner.close();
	}

}
