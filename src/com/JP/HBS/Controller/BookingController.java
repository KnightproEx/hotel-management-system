package com.JP.HBS.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.JP.HBS.Components.Snackbar;
import com.JP.HBS.IO.JSONFileReader;
import com.JP.HBS.IO.JSONFileWriter;
import com.JP.HBS.Model.Booking;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

public class BookingController {
	@FXML
	private JFXComboBox<String> roomCombobox;

	@FXML
	private DatePicker dateDatePicker;

	@FXML
	private JFXTextField durationTextfield, nameTextfield, icTextfield, emailTextfield, contact1Textfield,
			contact2Textfield;

	@FXML
	private AnchorPane mainPane;

	private JSONFileWriter fileWriter;
	private Snackbar snackbar;
	
	private final String[] roomArray = {"R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110",
			"R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210"};

	@FXML
	void initialize() {
		fileWriter = new JSONFileWriter("Data.json");
		
		dateDatePicker.valueProperty().addListener((observable, oldData, newData) -> {
			if (!isDurationValidate() || durationTextfield.getText().isBlank()) {
				roomCombobox.getItems().clear();
				return;
			}
			
			setComboboxData(newData, Integer.parseInt(durationTextfield.getText()));
		});
		
		durationTextfield.textProperty().addListener((observable, oldData, newData) -> {
			if (!isDurationValidate() || dateDatePicker.getValue() == null) {
				roomCombobox.getItems().clear();
				return;
			}
			
			setComboboxData(dateDatePicker.getValue(), Integer.parseInt(newData));
		});
	}
	
	private void setComboboxData(LocalDate date, int duration) {
		roomCombobox.setValue(null);
		roomCombobox.getItems().clear();
		
		try {
			for (int i = 0; i < roomArray.length; i++) {
				if (fileWriter.isAvailable(roomArray[i], date, duration)) {
					roomCombobox.getItems().add(roomArray[i]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isEmpty() {
		boolean validate = false;
		
		validate = durationTextfield.getText().isBlank() ? true : validate;
		validate = roomCombobox.getValue() == null ? true : validate;
		validate = dateDatePicker.getValue() == null ? true : validate;
		validate = nameTextfield.getText().isBlank() ? true : validate;
		validate = icTextfield.getText().isBlank() ? true : validate;
		validate = emailTextfield.getText().isBlank() ? true : validate;
		validate = contact1Textfield.getText().isBlank() ? true : validate;
		validate = contact2Textfield.getText().isBlank() ? true : validate;
		
		return validate;
	}
	
	private boolean isDurationValidate() {
		boolean validate = true;
		
		try {			
			if (Integer.parseInt(durationTextfield.getText()) < 1) throw new Exception();
		} catch(Exception e) {
			validate = false;
		}
		
		return validate;
	}
	
	private boolean isEmailValidate() {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = emailPattern.matcher(emailTextfield.getText());
        return matcher.find();
	}

	@FXML
	private void onButtonSubmit() {
		String bookingID = "";

		try {
			JSONFileReader fileReader = new JSONFileReader("Data.json");
			bookingID = fileReader.getBookingID();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		if (isEmpty()) {
			snackbar = new Snackbar(mainPane, "Please fill in all the fields!", 2);
			snackbar.show();
			return;
		}
		
		if (!isDurationValidate() || !isEmailValidate()) {
			snackbar = new Snackbar(mainPane, "Please use the correct data format!", 2);
			snackbar.show();
			return;
		}
		
		if (Integer.parseInt(durationTextfield.getText()) <= 0) {
			snackbar = new Snackbar(mainPane, "Duration must be larger than 0", 2);
			snackbar.show();
			return;
		}
		
		if (dateDatePicker.getValue().isBefore(LocalDate.now())) {
			snackbar = new Snackbar(mainPane, "Cannot book for the past!", 2);
			snackbar.show();
			return;
		}

		Booking booking = new Booking(
			Integer.parseInt(durationTextfield.getText()),
			bookingID,
			roomCombobox.getValue(),
			dateDatePicker.getValue().toString(),
			nameTextfield.getText(),
			icTextfield.getText(),
			emailTextfield.getText(),
			contact1Textfield.getText(),
			contact2Textfield.getText()
		);

		Alert alert = null;
		
		try {
			fileWriter.addRecord(booking);
			alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setHeaderText("Data inserted!");
		} catch (IOException e) {
			e.printStackTrace();
			alert = new Alert(AlertType.WARNING, "", ButtonType.OK);
			alert.setHeaderText("Something went wrong!");
		} finally {
			alert.showAndWait();			
		}
	}

	@FXML
	private void onButtonReset() {
		roomCombobox.setValue(null);
		dateDatePicker.setValue(null);
		durationTextfield.clear();
		nameTextfield.clear();
		icTextfield.clear();
		emailTextfield.clear();
		contact1Textfield.clear();
		contact2Textfield.clear();
	}
}
