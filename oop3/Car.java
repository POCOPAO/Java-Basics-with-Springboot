package oop3;

public class Car {
	private String make;
	private String model;
	private String oldColor;
	private String newColor;
	int year;

	public Car() {
		this.make = "";
		this.model = "";
		this.oldColor = "";
		this.newColor = "";
		this.year = 0;
		}

	
	public Car(String make, String model, int year, String oldColor, String newColor) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.oldColor = oldColor;
		this.newColor = newColor;
	}

	public String getBrand() {
		return make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getOldColor() {
		return oldColor;
	}
	
	public void setOldColor(String oldColor) {
		this.oldColor = oldColor;
	}
	
	public String setNewColor() {
		return newColor;
	}
	
	public void setNewColor(String newColor) {
		this.newColor = newColor;
	}
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	public void displayCarInfor(){
		System.out.println("Brand: " + make);
		System.out.println("Model: " + model);
		System.out.println("Released Year: " + year);
		System.out.println("Old Color: " + oldColor);
		System.out.println("Repaint Color: " + newColor);
		
	}
}