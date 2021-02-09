package com.JP.HBS.Controller;

import java.io.IOException;

import com.JP.HBS.Components.Snackbar;
import com.JP.HBS.Handler.SceneHandler;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

	private final String email = "admin";
	private final String pass = "admin";

	@FXML
	private JFXTextField emailTextField, passTextField;

	@FXML
	private AnchorPane mainPane;
	
	private Snackbar snackbar;
	private SceneHandler sceneHandler;
	
	@FXML
	private void onButtonSubmit(ActionEvent event) {
		if (!emailTextField.getText().equals(email) || !passTextField.getText().equals(pass)) {
			snackbar = new Snackbar(mainPane, "Invalid email or password!", 2);
			snackbar.show();
			return;
		}

		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			sceneHandler = new SceneHandler("Dashboard.fxml", stage);
			sceneHandler.showWindow("Hotel Management System");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
