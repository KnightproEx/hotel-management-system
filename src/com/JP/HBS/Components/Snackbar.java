package com.JP.HBS.Components;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Snackbar {

	private final Pane pane;
	private final String msg;
	private final int duration;
	private final JFXSnackbar snackbar;
	
	public Snackbar(Pane pane, String msg, int duration) {
		this.pane = pane;
		this.msg = msg;
		this.duration = duration;
		this.snackbar = new JFXSnackbar(this.pane);
	}
	
	public void show() {
		if (!this.snackbar.isVisible()) {
			this.snackbar.fireEvent(new SnackbarEvent(new JFXSnackbarLayout(this.msg), Duration.seconds(this.duration)));
		}
	}
	
}
