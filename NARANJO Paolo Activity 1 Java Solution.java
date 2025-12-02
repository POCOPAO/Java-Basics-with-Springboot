package com.bpi.helloworld.main;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner myName = new Scanner(System.in);
		
		System.out.println("What is your Name?");
		String userName = myName.nextLine();
		System.out.println("Hello, " + userName + "!");
	}

}
