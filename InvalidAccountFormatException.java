package com.java.exception.classes;

public class InvalidAccountFormatException extends RuntimeException{
    public InvalidAccountFormatException(String message){
        super(message);
    }
}
