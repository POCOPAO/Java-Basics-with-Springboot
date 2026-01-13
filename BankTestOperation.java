package com.java.logging;

public interface BankTestOperation {
    void execute() throws InvalidAmountException, InsufficientFundsException;
}
