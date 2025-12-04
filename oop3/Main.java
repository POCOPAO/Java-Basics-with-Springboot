package oop3;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Car car1 = new Car();
		Car car2 = new Car("Toyota", "Innova", 2020, "Picquant Orange", "Matte Gunpowder Black Metallic");
	
		car1.setMake("Honda");
		car1.setModel("City");
		car1.setYear(2019);
		car1.setOldColor("Pearl Jubilee White");
		car1.setNewColor("Virgin Beige");
		
		System.out.println("===== CLIENTS' CAR INFORMATION =====");
		
		System.out.println("\nClient 1's Car: ");
		car1.displayCarInfor();
		
		System.out.println("\nClient 2's Car: ");
		car2.displayCarInfor();
		
	}

}
