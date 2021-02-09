package com.JP.HBS.Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookingTable extends RecursiveTreeObject<BookingTable> {
	private final StringProperty id;
	private final StringProperty room;
	private final StringProperty date;
	private final StringProperty name;

	public BookingTable(String id, String room, String date, String name) {
		this.id = new SimpleStringProperty(!id.isBlank() ? id : "");
		this.room = new SimpleStringProperty(!room.isBlank() ? room : "");
		this.date = new SimpleStringProperty(!date.isBlank()? date : "");
		this.name = new SimpleStringProperty(!name.isBlank() ? name : "");
	}

	public StringProperty getID() { return id; }
	public StringProperty getRoom() { return room; }
	public StringProperty getDate() { return date; }
	public StringProperty getName() { return name; }
}
