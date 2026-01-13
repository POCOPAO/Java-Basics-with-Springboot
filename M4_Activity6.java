package com.java.logging;

public class M4_Activity6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BankAccount account = new BankAccount();

		BankAccount.runTest(() -> account.deposit(5000), "Deposit");
		
		BankAccount.runTest(() -> account.withdraw(3000), "Withdrawal");
		
		BankAccount.runTest(() -> account.deposit(-500), "Deposit");
		
		BankAccount.runTest(() -> account.withdraw(20000), "Withdrawal");
		
		BankAccount.runTest(() -> account.deposit(60000), "Deposit");
		
	}

}
