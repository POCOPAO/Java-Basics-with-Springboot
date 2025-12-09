package com.java.oop4;

public class Exercise2 {

	public static void main(String[] args) {
      // instantiate Car and Truck
		Car car = new Car(4, "Toyota");
		Truck truck = new Truck(6, "Volvo");
      // call their methods
		startEngine(car);
		refuelEngine(car);
		
		startEngine(truck);
		refuelEngine(truck);
		
		destroyVehicle(car);
		destroyVehicle(truck);
	}
	
	private static void startEngine(Vehicle vehicle) {
		
		vehicle.start();
		
	}
	
	private static void refuelEngine(Vehicle vehicle) {
		
		vehicle.refuel();
		
	}
	
	public static void destroyVehicle(Vehicle vehicle) {
		vehicle.destroy();
	}

}
