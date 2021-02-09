package com.JP.HBS.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.JP.HBS.Components.Snackbar;
import com.JP.HBS.Handler.SceneHandler;
import com.JP.HBS.IO.JSONFileWriter;
import com.JP.HBS.Model.Booking;
import com.JP.HBS.Singleton.BookingSingleton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UpdateBookingController {
	@FXML
	private Label bookingIDLabel;

	@FXML
	private JFXComboBox<String> roomCombobox;

	@FXML
	private DatePicker dateDatePicker;

	@FXML
	private JFXTextField durationTextfield, nameTextfield, icTextfield, contact1Textfield, emailTextfield,
			contact2Textfield;

	@FXML
	private AnchorPane mainPane;

	private JSONFileWriter fileWriter;
	private SceneHandler sceneHandler;
	private Booking booking;
	private Snackbar snackbar;
	
	private final String[] roomArray = {"R101", "R102", "R103", "R104", "R105", "R106", "R107", "R108", "R109", "R110",
			"R201", "R202", "R203", "R204", "R205", "R206", "R207", "R208", "R209", "R210"};

	@FXML
	void initialize() {
		fileWriter = new JSONFileWriter("Data.json");
		booking = BookingSingleton.getInstance().getBooking();
		bookingIDLabel.setText(booking.getID());
		onButtonReset();
		
		durationTextfield.textProperty().addListener((observable, oldData, newData) -> {
			if (!isDurationValidate() || dateDatePicker.getValue() == null) {
				roomCombobox.getItems().clear();
				return;
			}
			
			setComboboxData(dateDatePicker.getValue(), Integer.parseInt(newData));
		});
		
		dateDatePicker.valueProperty().addListener((observable, oldData, newData) -> {
			if (!isDurationValidate() || durationTextfield.getText().isBlank()) {
				roomCombobox.getItems().clear();
				return;
			}
			
			setComboboxData(newData, Integer.parseInt(durationTextfield.getText()));
		});
		
	}
	
	private void setComboboxData(LocalDate date, int duration) {
		roomCombobox.setValue(null);
		roomCombobox.getItems().clear();
		try {
			for (int i = 0; i < roomArray.length; i++) {
				if (fileWriter.isAvailable(roomArray[i], date, duration) || roomArray[i].equals(booking.getRoom())) {
					roomCombobox.getItems().add(roomArray[i]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onBackButtonClick(ActionEvent event) {
		try {
			sceneHandler = new SceneHandler("Dashboard.fxml",
					(Stage) ((Node) event.getSource()).getScene().getWindow());
			sceneHandler.showWindow("Hotel Management System");
			
			DashboardController db = sceneHandler.getLoader().getController();
			db.changePane("ManageBooking.fxml");
		} catch (IOException e) {
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
	private void onSubmit(ActionEvent event) {

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
		
		Booking booking = new Booking(
			Integer.parseInt(durationTextfield.getText()),
			this.booking.getID(),
			roomCombobox.getValue(),
			dateDatePicker.getValue().toString(),
			nameTextfield.getText(),
			icTextfield.getText(),
			emailTextfield.getText(),
			contact1Textfield.getText(),
			contact2Textfield.getText()
		);

		try {
			fileWriter.updateRecord("id", booking.getID(), booking);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		onBackButtonClick(event);
	}

	@FXML
	private void onButtonReset() {
		dateDatePicker.setValue(LocalDate.parse(booking.getDate()));
		durationTextfield.setText(String.valueOf(booking.getDuration()));
		nameTextfield.setText(booking.getName());
		icTextfield.setText(booking.getIC());
		emailTextfield.setText(booking.getEmail());
		contact1Textfield.setText(booking.getContact1());
		contact2Textfield.setText(booking.getContact2());
		setComboboxData(LocalDate.parse(booking.getDate()), booking.getDuration());
		roomCombobox.setValue(booking.getRoom());
	}
}
