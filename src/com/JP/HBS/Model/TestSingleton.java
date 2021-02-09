package com.JP.HBS.Model;

public class TestSingleton {
	
	private static TestSingleton instance = null;
	private String id;

	private TestSingleton() {}
	
	public static TestSingleton getInstance() {
		if (instance == null) {
			instance = new TestSingleton();
		}
		return instance;
	}
	
	public void setID(String id) { this.id = id; }
	public String getID() { return this.id; }
	
}
