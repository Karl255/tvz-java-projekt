package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableItemModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TimetableItem extends VBox {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");

	@FXML
	private Label timestampLabel;
	@FXML
	private Label titleLabel;

	private final TimetableItemModel model;

	public TimetableItem(ScheduleItem item) {
		this(new TimetableItemModel(item));
	}
	
	public TimetableItem(TimetableItemModel model) {
		this.model = model;
		
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/timetable-item-view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}
	
	@FXML
	private void initialize() {
		//this.titleLabel.setText(model.getScheduleItem().title());
		this.titleLabel.setText("temp");
		this.timestampLabel.setText(String.format("%s - %s",
			model.getScheduleItem().start().format(TIMESTAMP_FORMAT),
			model.getScheduleItem().end().format(TIMESTAMP_FORMAT)
		));
	}

	public TimetableItemModel getModel() {
		return model;
	}
	
	public ScheduleItem getScheduleItem() {
		return model.getScheduleItem();
	}
}
