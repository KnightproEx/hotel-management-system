package com.JP.HBS.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.JP.HBS.Handler.SceneHandler;
import com.JP.HBS.IO.JSONFileReader;
import com.JP.HBS.IO.JSONFileWriter;
import com.JP.HBS.Model.Booking;
import com.JP.HBS.Model.BookingTable;
import com.JP.HBS.Singleton.BookingSingleton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.stage.Stage;

public class ManageBookingController {

	@FXML
	private JFXTextField searchTextField;
	
	@FXML
	private JFXTreeTableView<BookingTable> tableView;

	@FXML
	private TreeTableColumn<BookingTable, String> idColumn, roomColumn, dateColumn, nameColumn;

	private ObservableList<BookingTable> data;
	private JSONArray bookingArray;
	private Map<String, Booking> bookingMap;
	private TreeTableViewSelectionModel<BookingTable> selectionModel;
	
	private JSONFileReader fileReader;
	private SceneHandler sceneHandler;
	private BookingSingleton booking;
	private BookingTable selectedBooking;

	@FXML
	void initialize() {
		bookingMap = new HashMap<String, Booking>();
		data = FXCollections.observableArrayList();
		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> onSearchTextChanged(newValue));

		try {
			fileReader = new JSONFileReader("Data.json");
			bookingArray = fileReader.readFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < bookingArray.size(); i++) {
			JSONObject bookingJSON = (JSONObject) bookingArray.get(i);
			Booking booking = Booking.convertObject(bookingJSON);
			
			bookingMap.put(booking.getID(), booking);
			addData(booking);
		}
		
		initializeTable();
		setTableData();
	}
	
	@FXML
	private void onViewButtonClick() {
		selectionModel = tableView.getSelectionModel();

		if (selectionModel.getSelectedIndex() < 0) {
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setHeaderText("Please select a booking!");
			alert.showAndWait();
			return;
		}

		try {
			selectedBooking = selectionModel.getSelectedItem().getValue();
			booking = BookingSingleton.getInstance();
			booking.setBooking(bookingMap.get(selectedBooking.getID().get()));
			sceneHandler = new SceneHandler("Receipt.fxml");
			sceneHandler.showWindow("Receipt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@FXML
	private void onUpdateButtonClick(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		selectionModel = tableView.getSelectionModel();

		if (selectionModel.getSelectedIndex() < 0) {
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setHeaderText("Please select a booking!");
			alert.showAndWait();
			return;
		}

		try {
			selectedBooking = selectionModel.getSelectedItem().getValue();
			booking = BookingSingleton.getInstance();
			booking.setBooking(bookingMap.get(selectedBooking.getID().get()));
			
			sceneHandler = new SceneHandler("Dashboard.fxml", stage);
			sceneHandler.showWindow("Receipt");
			
			((DashboardController) sceneHandler.getLoader().getController()).changePane("UpdateBooking.fxml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void onDeleteButtonClick() {
		selectionModel = tableView.getSelectionModel();
		
		if (selectionModel.getSelectedIndex() < 0) {
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setHeaderText("Please select a booking!");
			alert.showAndWait();
			return;
		}
		
		
		Alert alert = new Alert(AlertType.WARNING, "", ButtonType.YES, ButtonType.NO);
		alert.setHeaderText("This action cannot be undone!");
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
		    deleteBooking();
		}
	}
	
	private void deleteBooking() {
		try {
			selectedBooking = selectionModel.getSelectedItem().getValue();
			JSONFileWriter fileWriter = new JSONFileWriter("Data.json");
			fileWriter.deleteRecord("id", selectedBooking.getID().get());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			initialize();
		}
	}
	
	private void initializeTable() {
		idColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<BookingTable, String> param) -> param.getValue().getValue().getID());
		roomColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<BookingTable, String> param) -> param.getValue().getValue().getRoom());
		dateColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<BookingTable, String> param) -> param.getValue().getValue().getDate());
		nameColumn.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<BookingTable, String> param) -> param.getValue().getValue().getName());

		tableView.setShowRoot(false);
	}

	private void onSearchTextChanged(String text) {
		data.clear();
		
		try {
			bookingArray = fileReader.searchRecord(text.trim().replaceAll(" +", " "), "id", "date", "room", "name");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		for (int i = 0; i < bookingArray.size(); i++) {
			JSONObject bookingJSON = (JSONObject) bookingArray.get(i);
			Booking booking = Booking.convertObject(bookingJSON);
			addData(booking);
		}

		setTableData();
	}
	
	private void addData(Booking booking) {
		data.add(new BookingTable(booking.getID(), booking.getRoom(), booking.getDate(), booking.getName()));
	}

	private void setTableData() {
		tableView.setRoot(new RecursiveTreeItem<BookingTable>(data, RecursiveTreeObject::getChildren));
	}
}
