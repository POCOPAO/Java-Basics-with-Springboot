package com.bpi.helloworld.main;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner inputAge = new Scanner(System.in);
		System.out.println("Enter your age: ");
		int inAge = inputAge.nextInt();
		
		if (inAge < 18) {
			System.out.println("Minor");
			
	}	else if (inAge >= 18 && inAge < 60) {;
			System.out.println("Adult");
	}   else {
			System.out.println("Senior");
	}

	}
}
