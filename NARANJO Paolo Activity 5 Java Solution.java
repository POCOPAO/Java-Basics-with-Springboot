package com.bpi.helloworld.main;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    // Call method to add integers from 1 to 50
		int sumIntegers = addIntegers(1, 50);
		//Print result
		System.out.print("Sum = " + sumIntegers);
	}
	// Create method to add integers from 1 to 50
	public static int addIntegers(int a, int b) {
		//Initialize value of Sum
		int sumIntegers = 0;
		// For loop to add each number
		for (int i = a; i <= 50; i++) {
			sumIntegers += i;
		}
		return sumIntegers;
	}
	
}
