package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.CalendarItemModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class CalendarDay extends GridPane {
	@FXML
	private Label titleLabel;
	@FXML
	private AnchorPane contentAnchorPane;

	public CalendarDay() {
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/calendar-day-view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	public void setItems(List<ScheduleItem> items) {
		var elements = CalendarItemModel.organizeItems(items)
			.stream()
			.map(model -> {
				CalendarItem calendarItem = new CalendarItem(model);
				calendarItem.setOnMouseClicked(this::calednarItemClicked);
				return calendarItem;
			})
			.toList();

		contentAnchorPane.getChildren().setAll(elements);
		repositionItems();
	}

	public void repositionItems() {
		double width = contentAnchorPane.getBoundsInLocal().getWidth();
		double height = contentAnchorPane.getBoundsInLocal().getHeight();
		
		int maxColumn = contentAnchorPane.getChildren().stream()
			.map(c -> (CalendarItem) c)
			.max(Comparator.comparing(c -> c.getModel().getColumn()))
			.get()
			.getModel().getColumn();

		double columnWidth = width / (maxColumn + 1);

		for (var itemNode : contentAnchorPane.getChildren()) {
			if (itemNode instanceof CalendarItem item) {
				var model = item.getModel();

				AnchorPane.setTopAnchor(itemNode, height * model.getRelativeStart());
				AnchorPane.setBottomAnchor(itemNode, height * model.getRelativeEnd());

				int colsFromRight = (maxColumn - (model.getColumn() + model.getColumnSpan() - 1));
				AnchorPane.setLeftAnchor(itemNode, columnWidth * model.getColumn());
				AnchorPane.setRightAnchor(itemNode, columnWidth * colsFromRight);
			}
		}
	}

	public final ObjectProperty<EventHandler<MouseEvent>> onItemSelectedProperty() { return onItemSelected; }
	public final void setOnItemSelected(EventHandler<MouseEvent> value) { onItemSelectedProperty().setValue(value); }
	public final EventHandler<MouseEvent> getOnItemSelected() { return onItemSelectedProperty().get(); }
	private final ObjectProperty<EventHandler<MouseEvent>> onItemSelected = new ObjectPropertyBase<>() {
		@Override
		public Object getBean() {
			return CalendarDay.this;
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
