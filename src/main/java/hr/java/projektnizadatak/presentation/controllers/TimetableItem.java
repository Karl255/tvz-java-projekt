package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableDayItemModel;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TimetableItem extends VBox {
	private static final Logger logger = LoggerFactory.getLogger(TimetableItem.class);
	private final TimetableDayItemModel model;
	
	@FXML
	private Label timestampLabel;
	@FXML
	private Label titleLabel;

	public TimetableItem(ScheduleItem item) {
		this(new TimetableDayItemModel(item));
	}

	public TimetableItem(TimetableDayItemModel model) {
		this.model = model;

		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/timetable-item-view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			String m = "Loading fxml for " + getClass().getName();
			logger.error(m);
			
			throw new FxmlLoadingException(m, e);
		}
	}

	@FXML
	private void initialize() {
		this.titleLabel.setText(model.getScheduleItem().className());
		this.timestampLabel.setText(model.getScheduleItem().getTimestampShort());

		this.getStyleClass().add(
			switch (model.getScheduleItem().classType()) {
				case LECTURES -> "timetable-item-lecture";
				case AUDITORY_EXERCISES -> "timetable-item-auditory";
				case LAB -> "timetable-item-lab";
				case OTHER -> "";
			}
		);
	}

	public TimetableDayItemModel getModel() {
		return model;
	}

	public ScheduleItem getScheduleItem() {
		return model.getScheduleItem();
	}
}
