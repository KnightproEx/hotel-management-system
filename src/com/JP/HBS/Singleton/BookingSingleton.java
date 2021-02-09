package com.JP.HBS.Singleton;

import com.JP.HBS.Model.Booking;

public class BookingSingleton {
	private static BookingSingleton instance = null;
	private Booking booking;

	private BookingSingleton() {}
	
	public static BookingSingleton getInstance() {
		if (instance == null) {
			instance = new BookingSingleton();
		}
		return instance;
	}
	
	public void setBooking(Booking booking) { this.booking = booking; }
	public Booking getBooking() { return this.booking; }
}
