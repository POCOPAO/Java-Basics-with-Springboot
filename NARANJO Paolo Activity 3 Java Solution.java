package com.bpi.helloworld.main;
import java.util.Scanner;

public class Main {
	public static int IntSum(int a, int b) {
		return a + b;
	}
	public static int  IntDiff(int a, int b) {
		return a - b;
	}
	public static int  IntProd(int a, int b) {
		return a * b;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		System.out.print("Enter first integer: ");
		int in1 = input.nextInt();
		System.out.print("Enter second integer: ");
		int in2 = input.nextInt();
		
		System.out.println("Sum : " + IntSum(in1, in2));
		System.out.println("Difference : " + IntDiff(in1, in2));
		System.out.println("Product : " + IntProd(in1, in2));
		input.close();
	}
}
