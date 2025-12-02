package com.bpi.helloworld.main;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myAge = new Scanner(System.in);
		
		System.out.println("Enter your Age: ");
		String userAge = myAge.nextLine();
		int userAgeInt = Integer.parseInt(userAge);
		double userAgeDouble = Double.parseDouble(userAge);
		System.out.println("Your age as int: " + userAgeInt);
		System.out.println("Your age as double: " + userAgeDouble);
		
	}

}
