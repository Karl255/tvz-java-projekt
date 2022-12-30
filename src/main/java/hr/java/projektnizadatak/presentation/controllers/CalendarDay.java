package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.presentation.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

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
	
	public void setItems(List<CalendarItem> items) {
		contentAnchorPane.getChildren().setAll(items);
		repositionItems();
	}
	
	public void repositionItems() {
		double width = contentAnchorPane.getBoundsInLocal().getWidth();
		double height = contentAnchorPane.getBoundsInLocal().getHeight();
		
		int maxColumn = contentAnchorPane.getChildren().stream()
			.map(c -> (CalendarItem)c)
			.max(Comparator.comparing(c -> c.getModel().getColumn()))
			.get()
			.getModel().getColumn();
		
		double columnWidth = width / (maxColumn + 1);
		
		for (var itemNode : contentAnchorPane.getChildren()) {
			if (itemNode instanceof CalendarItem item) {
				var model = item.getModel();
				
				AnchorPane.setTopAnchor(itemNode, height * model.getRelativeStart());
				AnchorPane.setBottomAnchor(itemNode, height * model.getRelativeEnd());
				
				AnchorPane.setLeftAnchor(itemNode, columnWidth * model.getColumn());
				AnchorPane.setRightAnchor(itemNode, columnWidth * (maxColumn - model.getColumn() - model.getColumnSpan() + 1));
				//System.out.printf("(%d, %d) -> (%d, %d)%n", model.getColumn(), model.getColumnSpan(), model.getColumn(), (maxColumn - model.getColumn() - model.getColumnSpan() + 1));
			}
		}
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
