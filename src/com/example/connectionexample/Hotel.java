package com.example.connectionexample;

/**
 * Para representar un hotel.
 * 
 * @author sanrodari
 */
public class Hotel {
	
	private long id;
	private String name;
	private double valueReservation;
	
	public Hotel() {
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getValueReservation() {
		return valueReservation;
	}
	public void setValueReservation(double valueReservation) {
		this.valueReservation = valueReservation;
	}

}
