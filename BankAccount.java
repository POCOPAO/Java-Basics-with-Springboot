package com.java.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BankAccount {
    private static final Logger logger = LoggerFactory.getLogger(BankAccount.class);
    private double balance = 10000.0;

    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException{
        logger.info("Withdrawal requested: P{}", amount);
        if (amount <= 0){
            logger.error("Invalid withdrawal amount: P{}", amount);
            throw new InvalidAmountException("Amount must be positive.");
        }
        if (amount > balance){
            logger.warn("Insufficient funds: P{} available", balance);
            throw new InsufficientFundsException("Insufficient funds for withdrawal.", balance, amount);
        }
        balance -= amount;
        logger.info("Withdrawal completed: P{}, New balance: P{}", amount, balance);
    }

    public void deposit(double amount) throws InvalidAmountException{
        logger.info("Deposit requested: P{}", amount);
        if (amount <= 0){
            logger.error("Invalid deposit amount: P{}", amount);
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        if (amount > 50000){
            logger.warn("Large deposit: P{} - requires verification", amount);
        }
        balance += amount;
        logger.info("Deposit completed: P{}, New balance: P{}", amount, balance);
    }

    public static void runTest(BankTestOperation operation, String operationName){
        try {
            operation.execute();
        } catch (InvalidAmountException e) {
            logger.error("Deposit failed: Deposit amount must be positive", e.getMessage(), e);
        } catch (InsufficientFundsException e){
            logger.error("Withdrawal failed: Insufficient funds for withdrawal", e.getMessage(), e);
        }
    }
}
