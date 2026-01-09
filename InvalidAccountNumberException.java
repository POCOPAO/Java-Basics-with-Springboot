package com.java.exception.classes;

public class InvalidAccountNumberException extends Exception{
    public InvalidAccountNumberException(String message){
        super(message);
    }
}
