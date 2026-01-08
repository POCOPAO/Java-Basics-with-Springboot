package com.java.exception;

public class ATMSystem {
	static double[] accounts = {10000, 15000, 20000};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("=== ATM Withdrawal System ===");
		
		System.out.println("\n--- Test 1: Valid Withdrawal ---");
		processWithdrawals("1", "5000");

		System.out.println("\n--- Test 2: Invalid Account Index ---");
		processWithdrawals("abc", "5000");

		System.out.println("\n--- Test 3: Account Not Found ---");
		processWithdrawals("10", "5000");

		System.out.println("\n--- Test 4: Insufficient Funds ---");
		processWithdrawals("1", "20000");
		
		System.out.println("\n=== All tests completed! ===");
		
	}
	
	
	public static void processWithdrawals(String accountIndex, String amountInput) {
		
		try {
			int index = Integer.parseInt(accountIndex);
			
			double balance = accounts[index];
			
			double amount = Double.parseDouble(amountInput);
			
			if (amount > balance) {
				System.out.println("Account=" + index +", Amount=" + amount);
				System.out.println("Current balance: P" + balance);
				System.out.println("Withdrawal :P" + amount);
				System.out.println("Insufficient funds! Cannot withdraw P" + amount);
			} else {
				accounts[index] = balance;
				System.out.println("Account=" + index +", Amount=" + amount);
				System.out.println("Current balance: P" + balance);

				balance -= amount;
				System.out.println("Withdrawal :P" + amount);
				System.out.println("New balance: P" + balance);
				System.out.println("Withdrawal successful!");
			}
		} catch (NumberFormatException e) {
			System.out.println("Account=" + accountIndex +", Amount=" + amountInput);
			System.out.println("Error: Invalid input!");
			System.out.println("Please enter valid numbers.");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Account=" + accountIndex +", Amount=" + amountInput);
			System.out.println("Error: Account not found!");
			System.out.println("Invalid account index.");
		} catch (Exception e) {
			System.out.println("Transaction failed");
		}
	}

	
}
