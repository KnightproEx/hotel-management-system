package com.JP.HBS.Controller;

import java.io.IOException;

import com.JP.HBS.Main;
import com.JP.HBS.Handler.SceneHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController {

	@FXML
	private BorderPane mainPane;

	private SceneHandler sceneHandler;

	@FXML
	void initialize() {
		changePane("Booking.fxml");
	}

	@FXML
	private void onNewBookingButtonClick(ActionEvent event) {
		changePane("Booking.fxml");
	}

	@FXML
	private void onManageBookingButtonClick(ActionEvent event) {
		changePane("ManageBooking.fxml");
	}

	@FXML
	private void onLogoutButtonClick(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
		alert.setHeaderText("Confirm logout?");
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
		    return;
		}
		
		try {
			sceneHandler = new SceneHandler("Login.fxml", (Stage) ((Node) event.getSource()).getScene().getWindow());
			sceneHandler.showWindow("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changePane(String filename) {
		try {
			mainPane.setCenter(FXMLLoader.load(Main.class.getResource("Scene/" + filename)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	private FXMLLoader changePane(String filename) {
//		URL fileURL = Main.class.getResource("Scene/" + filename);
//
//		try {
//			if (fileURL == null) throw new FileNotFoundException(filename + " not found!");
//			FXMLLoader loader = new FXMLLoader(fileURL);
//			mainPane.setCenter(loader.load());
//			return loader;
//		}
//		
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}

}
