package com.java.oop4;

public class Exercise2 {

	public static void main(String[] args) {
      // instantiate Car and Truck
		Car car = new Car(4, "Toyota");
		Truck truck = new Truck(6, "Volvo");
      // call their methods
		start(car);
		car.refuel();
		
		start(truck);
		truck.refuel();
		
		destroyVehicle(car);
		destroyVehicle(truck);
	}
	
	public static void start(Vehicle vehicle) {
		vehicle.startEngine();
	}
	
	
	public static void destroyVehicle(Vehicle vehicle) {
		vehicle.destroy();
	}

}
