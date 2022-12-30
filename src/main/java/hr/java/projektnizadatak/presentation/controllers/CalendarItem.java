package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.CalendarItemModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class CalendarItem extends VBox {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");

	@FXML
	private Label timestampLabel;
	@FXML
	private Label titleLabel;

	private final CalendarItemModel model;

	public CalendarItem(ScheduleItem item) {
		this(new CalendarItemModel(item));
	}
	
	public CalendarItem(CalendarItemModel model) {
		this.model = model;
		
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
	
	public void initialize() {
		//this.titleLabel.setText(model.getScheduleItem().title());
		this.titleLabel.setText("temp");
		this.timestampLabel.setText(String.format("%s - %s",
			model.getScheduleItem().start().format(TIMESTAMP_FORMAT),
			model.getScheduleItem().end().format(TIMESTAMP_FORMAT)
		));
	}

	public CalendarItemModel getModel() {
		return model;
	}
}
