package com.JP.HBS.Controller;

import com.JP.HBS.Model.Booking;
import com.JP.HBS.Singleton.BookingSingleton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReceiptController {

	@FXML
	private Label idLabel, dateLabel, durationLabel, subtotalLabel, taxLabel, totalLabel;

	@FXML
	private JFXTextField nameTextfield, roomTextfield, icTextfield, emailTextfield, contact1Textfield, contact2Textfield;

	@FXML
	void initialize() {
		Booking booking = BookingSingleton.getInstance().getBooking();
		
		final double subtotal = Math.round(((booking.getDuration() * 350)) * 100.0 / 100);
		final double tax = Math.round(((booking.getDuration() * 350 * 0.1)) * 100.0 / 100);
		final double total = Math.round(((booking.getDuration() * 350 * 1.1) + (booking.getDuration() * 10)) * 100.0 / 100);

		idLabel.setText(booking.getID());
		dateLabel.setText(booking.getDate());
		durationLabel.setText(String.valueOf(booking.getDuration() + " days"));
		subtotalLabel.setText("$" + String.format("%.2f", subtotal));
		taxLabel.setText("$" + String.format("%.2f", tax));
		totalLabel.setText("$" + String.format("%.2f", total));
		
		nameTextfield.setText(booking.getName());
		roomTextfield.setText(booking.getRoom());
		icTextfield.setText(booking.getIC());
		emailTextfield.setText(booking.getEmail());
		contact1Textfield.setText(booking.getContact1());
		contact2Textfield.setText(booking.getContact2());
	}
}
