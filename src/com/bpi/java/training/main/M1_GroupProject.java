package com.bpi.java.training.main;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class group
 * 
 * GROUP PROJECT
	Objective: Apply Basic Java learning
	Create a console-based Student Grade Management System
	The system must allow the user to perform the following actions:
	1. Input student details
	2. Input number of subjects
	3. Input grades per subject
	4. Compute average grades of all subjects
	5. Determine if grade is pass or fail
	Student details must contain the following information:
	- Student name
	- Student ID
	- Number of subjects
	- Grades for each subject
 */
public class M1_GroupProject {
	
	static String studentName="";
	static String studentId="";
	static String status = "";
	static String formattedAverage = "";
	static int numOfSubjects = 0;
	static int[] grades = null;
	static double averageGrade = 0;
	static boolean studentAdded = false;
	
	//main method
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		// TODO Auto-generated method stub
		
	
		char choice;
		do {
			
			showMenu();

			System.out.print("Enter choice: ");
			
			choice = input.next().toUpperCase().charAt(0);
			
			switch(choice) {
			case 'A':
			addStudent(input);
			break;
			case 'B':
				if (!studentAdded) {
					System.out.println("Please enter student info first (Option A).");
				} else {
					computeAverage();
				}
				//TODO
			break;	
			case 'C':
				if (!studentAdded) {
					System.out.println("No student data yet. Please choose option A in the menu.");
				} else {
					displayInfo();
				}
				//TODO
			break;
			case 'D':
				//TODO
				System.out.println("Thank you for using the system!");
                
			break;
			
			default:
				 System.out.println("choice is A,B,C,D only...");
			}
		}while(choice!='D');
		
		System.out.println("Exited...");

		input.close();

	}
	//end main method
 
	/*show menu method
	 *  ===== STUDENT GRADING SYSTEM =====
		A - Add Student Information
		B - Compute Student Average
		C - Display Student Information
		D - Exit
	 */
	static void showMenu() {
		System.out.println("\n===== STUDENT GRADING SYSTEM =====");
		System.out.println("A - Add Student Information");
		System.out.println("B - Compute Student Average");
		System.out.println("C - Display Student Information");
		System.out.println("D - Exit");
		System.out.println("");
	}
	//end show menu method
	
	//add student method
	static void addStudent(Scanner input) {
		input.nextLine(); //Wait for input before next line
		/*
		 * Initialize status and Average Grade
		 */
		status = "";
		formattedAverage = "0.0";
		/*
		 *  Enter choice: A
			Enter student name: JUAN DELA CRUZ
			Enter student ID: 20251124
			Enter number of subjects: 2
			Enter grade for subject 1: 90
			Enter grade for subject 2: 87
			===== STUDENT SAVED =====
		 * 
		 */
		System.out.print("Enter student name: ");
		studentName=input.nextLine();
		 
		System.out.print("Enter student ID: ");
		studentId=input.nextLine();
		 
		numOfSubjects=handleNonNumeric(input, "Enter number of subjects: ");
		 
		grades = new int[numOfSubjects];
		int gradeNumber=0;
		
		for(int i=0; i<numOfSubjects;i++) {
			//Enter grade for subject 1: 90
			gradeNumber = i+1;
			grades[i]=handleNonNumeric(input, "Enter grade for subject " + gradeNumber + ": ");
			
		 }
		studentAdded = true;
		System.out.println("===== STUDENT INFO SAVED =====");

	}
	//end add student method
	
	// Handle non-numerical input
			public static int handleNonNumeric(Scanner input, String message) {
				while (true) {
					System.out.print(message);
					try {
						return input.nextInt();
					} catch (InputMismatchException e) {
						System.out.println("Invalid input. Please enter a valid number");
						input.nextLine();
					}
					
					
				}
			}
	//end Handle non-numerical input
	
	
	//compute student method
	public static void computeAverage() {
		int sum = 0;
		for (int grade : grades) {
			sum += grade;
		}
		averageGrade = sum / (double) numOfSubjects;
		formattedAverage = String.format("%.2f", averageGrade);
		System.out.println("Average: " + formattedAverage);
		// Determine Status 
		if (averageGrade > 75) {
			status = "PASS";
	     } else {
	    	 status = "FAIL";	 
        }
		System.out.println("Status: " + status);
		
	}
	
	//end compute student method
	
	//student info method
		public static void displayInfo() {
			System.out.println("\n===== STUDENT SUMMARY =====");
			System.out.println("Student Name: " + studentName);
			System.out.println("Student Number: " + studentId);
			System.out.println("Average Grade: " + formattedAverage);
			System.out.println("Status: " + status);
			

			}
	//end student info method
	
} // end group class

