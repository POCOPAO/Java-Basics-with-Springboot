package com.java.list;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


public class MapModule {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		Map<String, Double> products = new LinkedHashMap<>();
		products.put("Chicken", 180.00);
		products.put("Beef", 399.99);
		products.put("Pork", 300.00);
		
		int option;
		
		do {
			System.out.println("\nSelect an Option:");
			System.out.println("1. Search a product");
			System.out.println("2. Add a product");
			System.out.println("3. Print all products and prices");
			System.out.println("4. Find the cheapest product");
			System.out.println("5. Exit");
			System.out.print(">");
						
			option = sc.nextInt();
			sc.nextLine();
			
			switch (option) {
			case 1: 
				System.out.print("Enter product name to search: ");
				String searchProduct = sc.nextLine();
				if (products.containsKey(searchProduct)) {
					System.out.println("Product found! Price: " + products.get(searchProduct));
				} else {
					System.out.println("Product not found!");
				}
				break;
			case 2:
				System.out.print("Enter a product name: ");
				String productName = sc.nextLine();
				System.out.print("Enter product price: ");
				double productPrice = sc.nextDouble();
				products.put(productName, productPrice);
				System.out.print("Product added: " + productName);
				break;
			case 3:
				System.out.println("All products and prices: ");
				for (Map.Entry<String, Double> entry : products.entrySet()) {
					System.out.println(entry.getKey()+ " - " + entry.getValue());
				}
				break;
			case 4:
				String cheapestProduct = null;
				double cheapestPrice = Double.MAX_VALUE;
				
				for (Map.Entry<String, Double> entry : products.entrySet()) {
					if (entry.getValue() < cheapestPrice) {
						cheapestPrice = entry.getValue();
						cheapestProduct = entry.getKey();
						
					}
				}
				
				if (cheapestProduct != null) {
					System.out.println("Cheapest product: " + cheapestProduct + " - " + cheapestPrice);
				}
				break;
			case 5:
				System.out.println("Exiting...");
				break;
				
			default:
				System.out.print("Invalid choice");
			}
			
		} while (option != 5);
		sc.close();
	}

}
