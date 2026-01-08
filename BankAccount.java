package com.java.exception;

public class BankAccount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("=== Bank Account Name Display ===");
	
		testCase("ACC-001");
		
		testCase("ACC-999");
		
		System.out.println("\n\n=== Program completed successfully!");
	}
	
	public static String getAccountName(String accountNumber) {
		if (accountNumber == "ACC-001") {
			return "Juan Dela Cruz";
		} else if (accountNumber == "ACC-002") {
			return "Maria Santos";
		} else {
			return null;
		}
	}

	public static void testCase(String accountNumber) {
		System.out.println("\nLooking up account: " + accountNumber);
		
		try {
			String name = getAccountName(accountNumber);
			String upperName = name.toUpperCase();
			System.out.println("Account holder: " + upperName);
		} catch(NullPointerException e) {
			System.out.println("Error: Account not found!");
		}
	}
}
