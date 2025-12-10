package com.java.oop4;
/**
** This an abstract class with attributes
** - numberOfWheels
** - brand
** and methods
** - a concrete method destroy()
** - an abstract method startEngine()

*/
public abstract class Vehicle {
	private int numberOfWheels;
	private String brand;
	
	public Vehicle() {
	}
	
	public Vehicle(int numberOfWheels, String brand) {
		this.numberOfWheels = numberOfWheels;
		this.brand = brand;
	}
	
	abstract void startEngine();
	
	public int getNumberOfWheels() {
		return numberOfWheels;
	}
	
	public void setNumberOfWheels(int numberOfWheels) {
		this.numberOfWheels = numberOfWheels;
	}
	
	public String getBrand() {
		return brand;	
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public void destroy() {
		System.out.println(brand + " vehicle has been destroyed!");
	}
}
