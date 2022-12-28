package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CalendarItemController extends VBox {
	@FXML
	private Label timestamp;
	@FXML
	private Label title;
	
	public CalendarItemController() {
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/calendar-item-view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public String getTimestamp() {
		return timestamp.getText();
	}

	public void setTimestamp(String text) {
		timestamp.setText(text);
	}
	
	public StringProperty timestampProperty() {
		return timestamp.textProperty();
	}
	
	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String text) {
		title.setText(text);
	}
	
	public StringProperty titileProperty() {
		return title.textProperty();
	}
	
	
}
