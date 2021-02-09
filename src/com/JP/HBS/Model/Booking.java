package com.JP.HBS.Model;

import org.json.simple.JSONObject;

public class Booking {
	private int duration;
	private String id;
	private String room;
	private String date;
	private String name;
	private String ic;
	private String email;
	private String contact1;
	private String contact2;
	
	public Booking(int day, String... dataArg) {
		this.duration = day;
		this.id = dataArg[0];
		this.room = dataArg[1];
		this.date = dataArg[2];
		this.name = dataArg[3];
		this.ic = dataArg[4];
		this.email = dataArg[5];
		this.contact1 = dataArg[6];
		this.contact2 = dataArg[7];
	}
	
	public int getDuration() { return duration; }
	public String getID() { return id; }
	public String getRoom() { return room; }
	public String getDate() { return date; }
	public String getName() { return name; }
	public String getIC() { return ic; }
	public String getEmail() { return email; }
	public String getContact1() { return contact1; }
	public String getContact2() { return contact2; }

	public static Booking convertObject(JSONObject obj) {
		int duration = ((Long) obj.get("duration")).intValue();
		String id = obj.get("id").toString();
		String room = obj.get("room").toString();
		String date = obj.get("date").toString();
		String name = obj.get("name").toString();
		String ic = obj.get("ic").toString();
		String email = obj.get("email").toString();
		String contact1 = obj.get("contact1").toString();
		String contact2 = obj.get("contact2").toString();
		
		return new Booking(duration, id, room, date, name, ic, email, contact1, contact2);
	}
}
