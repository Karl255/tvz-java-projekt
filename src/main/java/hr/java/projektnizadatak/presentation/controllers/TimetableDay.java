package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.TimetableItemModel;
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

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class TimetableDay extends GridPane {
	@FXML
	private Label titleLabel;
	@FXML
	private AnchorPane contentAnchorPane;

	public TimetableDay() {
		FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/timetable-day-view.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new FxmlLoadingException("Loading fxml for " + getClass().getName(), e);
		}
	}

	public void setItems(List<ScheduleItem> items) {
		var elements = TimetableItemModel.organizeItems(items)
			.stream()
			.map(model -> {
				TimetableItem timetableItem = new TimetableItem(model);
				timetableItem.setOnMouseClicked(this::calednarItemClicked);
				return timetableItem;
			})
			.toList();

		contentAnchorPane.getChildren().setAll(elements);
		repositionItems();
	}

	public void repositionItems() {
		if (contentAnchorPane.getChildren().size() == 0) {
			return;
		}
		
		double width = contentAnchorPane.getBoundsInLocal().getWidth();
		double height = contentAnchorPane.getBoundsInLocal().getHeight();
		
		int maxColumn = contentAnchorPane.getChildren().stream()
			.map(item -> (TimetableItem) item)
			.max(Comparator.comparing(item -> item.getModel().getColumn()))
			.orElseThrow(() -> new UnreachableCodeException("No maximum found while repositioning items"))
			.getModel().getColumn();

		double columnWidth = width / (maxColumn + 1);

		for (var itemNode : contentAnchorPane.getChildren()) {
			if (itemNode instanceof TimetableItem item) {
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
