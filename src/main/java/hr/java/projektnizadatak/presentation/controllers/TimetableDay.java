package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableDayItemModel;
import hr.java.projektnizadatak.shared.exceptions.FxmlLoadingException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class TimetableDay extends GridPane {
	private static final Logger logger = LoggerFactory.getLogger(TimetableDay.class);

	@FXML
	private Label titleLabel;
	@FXML
	private AnchorPane contentAnchorPane;
	
	private List<TimetableItem> timetableItems;

	public TimetableDay() {
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/timetable-day-view.fxml"));
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

	public void setItems(List<ScheduleItem> items) {
		contentAnchorPane.getChildren().clear();

		timetableItems = TimetableDayItemModel.organizeItems(items)
			.stream()
			.map(model -> {
				TimetableItem timetableItem = new TimetableItem(model);
				timetableItem.setOnMouseClicked(this::calednarItemClicked);
				return timetableItem;
			})
			.toList();

		repositionItems();
	}

	public void repositionItems() {
		if (timetableItems == null) {
			return;
		}
		
		if (timetableItems.size() == 0) {
			return;
		}

		double width = contentAnchorPane.getBoundsInLocal().getWidth();
		double height = contentAnchorPane.getBoundsInLocal().getHeight();

		contentAnchorPane.getChildren().setAll(timetableItems);

		int maxColumn = contentAnchorPane.getChildren().stream()
			.map(item -> (TimetableItem) item)
			.max(Comparator.comparing(item -> item.getModel().getColumn()))
			.orElseThrow(() -> {
				String m = "No maximum found while repositioning items";
				logger.error(m);

				return new UnreachableCodeException(m);
			})
			.getModel().getColumn();

		double columnWidth = width / (maxColumn + 1);

		for (var item : timetableItems) {
			var model = item.getModel();

			AnchorPane.setTopAnchor(item, height * model.getRelativeStart());
			AnchorPane.setBottomAnchor(item, height * model.getRelativeEnd());

			int colsFromRight = (maxColumn - (model.getColumn() + model.getColumnSpan() - 1));
			AnchorPane.setLeftAnchor(item, columnWidth * model.getColumn());
			AnchorPane.setRightAnchor(item, columnWidth * colsFromRight);

		}
	}

	public final ObjectProperty<EventHandler<MouseEvent>> onItemSelectedProperty() {return onItemSelected;}
	public final EventHandler<MouseEvent> getOnItemSelected() {return onItemSelectedProperty().get();}
	public final void setOnItemSelected(EventHandler<MouseEvent> value) {onItemSelectedProperty().setValue(value);}
	private final ObjectProperty<EventHandler<MouseEvent>> onItemSelected = new ObjectPropertyBase<>() {
		@Override
		public Object getBean() {
			return TimetableDay.this;
		}

		@Override
		public String getName() {
			return "itemSelected";
		}
	};

	private void calednarItemClicked(MouseEvent e) {
		getOnItemSelected().handle(e);
	}

	public String getTitle() {
		return titleLabel.getText();
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}

	public StringProperty titleProperty() {
		return titleLabel.textProperty();
	}
}
