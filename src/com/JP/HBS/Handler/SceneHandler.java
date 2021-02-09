package com.JP.HBS.Handler;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneHandler {
	private Stage stage;
	private FXMLLoader loader;
	private Scene scene;

	// change existing window
	public SceneHandler(String file, Stage stage) {
		this.stage = stage;
		this.loader = new FXMLLoader(getClass().getResource("../Scene/" + file));
	}

	// open new window
	public SceneHandler(String file) {
		this.stage = new Stage();
		this.loader = new FXMLLoader(getClass().getResource("../Scene/" + file));
	}

	public void showWindow(String title) throws IOException {
		scene = new Scene(this.loader.load());
		stage.setScene(this.scene);
		stage.setTitle(title);
		stage.setResizable(false);
		stage.show();
	}

	public FXMLLoader getLoader() {
		return loader;
	}

}
