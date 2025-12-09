package com.java.oop4;
/*
** This is a concrete class, it is a Vehicle and it is Refuelable
*/
public class Car extends Vehicle implements Refuelable{
	public Car(int numberOfWheels,String brand) {
		this.setNumberOfWheels(numberOfWheels);
		this.setBrand(brand);
	}
	
	public void start() {
		String message = "This " + this.getNumberOfWheels() + "-wheeled vehicle's engine has been started! It's a " + this.getBrand() + " Car";
		System.out.println(message);
		
	}
	
	public void refuel() {
		String message = "The " + this.getBrand() + " Car is now being refueled...";
		System.out.println(message);
	}
}
