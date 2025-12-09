package com.java.oop4;

public class MSExcel extends Program{
	
	public MSExcel(String name) {
		 this.setName(name);
	}
	
	@Override
	public void run() {
		String message = "Opening MS Excel...";
		setIsRunning(true);
		
		System.out.println(message);
	}
	
	@Override
	public void stop() {
		String message = "Stopping MS Excel...";
		setIsRunning(false);
		
		System.out.println(message);
	}
}
