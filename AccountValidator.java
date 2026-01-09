package com.java.exception;

public class AccountValidator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] testCases = {"1234567890", "123", null};
		
		for (String account : testCases) {
			try {
				validateAccountNumber(account);
		} catch (NullPointerException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		}
	}

	public static void validateAccountNumber(String accountNumber) throws Exception {
		if (accountNumber == null) {
			throw new NullPointerException("Cannot be null");
		}
		if (accountNumber.length() != 10) {
			throw new Exception("Must be 10 digits");
		}
		System.out.println("Valid account: " + accountNumber);
	}
}
