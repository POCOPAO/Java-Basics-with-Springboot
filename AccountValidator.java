package com.java.exception.classes;

public class AccountValidator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("=== Account Number Validation Test ===\n");

		testValidation("Test 1: Valid account (1234567890)", "1234567890");

		testValidation("Test 2: Too short (123)", "123");

		testValidation("Test 3: Contatins letter (12345ABC90)", "12345ABC90");

		testValidation("Test 4: Contains space (1234 567890)", "1234 567890");

		testValidation("Test 5: Null value", null);
	}

	public static void validateAccountNumber(String accountNumber) throws InvalidAccountNumberException{
		if (accountNumber == null){
			throw new NullPointerException("Account number cannot be null");
		}

		for (char c : accountNumber.toCharArray()){
			if(!Character.isDigit(c)){
				throw new InvalidAccountFormatException("Account number must contain only digits");
			}
		}

		if (accountNumber.length() != 10){
			throw new InvalidAccountNumberException("Account number must be excatly 10 digits");
		}

		System.out.println("Valid account number: " + accountNumber);
	}

	public static void testValidation(String testName, String accountNumber){
		System.out.println(testName);
			try {
				validateAccountNumber(accountNumber);
			} catch (InvalidAccountNumberException e) {
				System.out.println("Error: " + e.getMessage());
			} catch (InvalidAccountFormatException e) {
				System.out.println("Warning : " + e.getMessage());
			} catch (NullPointerException e ){
				System.err.println("Warning : " + e.getMessage());
			}
			System.err.println();
		}
	}

