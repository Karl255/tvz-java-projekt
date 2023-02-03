package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableDayModel;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TimetableItemModel extends TextFlow {
	private static final Logger logger = LoggerFactory.getLogger(TimetableItemModel.class);
	private final TimetableDayModel model;
	
	public TimetableItemModel(ScheduleItem item) {
		this(new TimetableDayModel(item));
	}

	public TimetableItemModel(TimetableDayModel model) {
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
		var timestamp = new Text(model.getScheduleItem().getTimestampShort());
		var label = new Text(model.getScheduleItem().className());
		label.getStyleClass().add("bold");
		
		getChildren().setAll(timestamp, new Text("   "), label);

		getStyleClass().add(
			switch (model.getScheduleItem().classType()) {
				case LECTURES -> "timetable-item--lecture";
				case AUDITORY_EXERCISES -> "timetable-item--auditory";
				case LAB -> "timetable-item--lab";
				case SPORTS_ACTIVITY -> "timetable-item--sports";
				case OTHER -> "";
			}
		);
	}

	public TimetableDayModel getModel() {
		return model;
	}

	public ScheduleItem getScheduleItem() {
		return model.getScheduleItem();
	}
}
