package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableItemModel;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TimetableItem extends VBox {
	private final TimetableItemModel model;
	
	@FXML
	private Label timestampLabel;
	@FXML
	private Label titleLabel;

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
			throw new FxmlLoadingException("Loading fxml for " + getClass().getName(), e);
		}
	}

	@FXML
	private void initialize() {
		this.titleLabel.setText(model.getScheduleItem().className());
		this.timestampLabel.setText(model.getScheduleItem().getTimestamp());
	}

	public TimetableItemModel getModel() {
		return model;
	}

	public ScheduleItem getScheduleItem() {
		return model.getScheduleItem();
	}
}
