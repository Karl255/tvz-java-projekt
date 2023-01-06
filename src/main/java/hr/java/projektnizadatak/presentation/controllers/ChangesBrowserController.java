package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.ChangesManager;
import hr.java.projektnizadatak.application.entities.Change;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;

public class ChangesBrowserController {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
	
	@FXML private TextFlow oldValueDetails;
	@FXML private TextFlow newValueDetails;
	
	@FXML private TableView<Change> changeTableView;
	@FXML private TableColumn<Change, String> usernameColumn;
	@FXML private TableColumn<Change, String> timestampColumn;
	@FXML private TableColumn<Change, String> oldValueColumn;
	@FXML private TableColumn<Change, String> newValueColumn;
	
	@FXML
	private void initialize() {
		usernameColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().username()));
		timestampColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().timestamp().format(TIMESTAMP_FORMAT)));
		oldValueColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().oldValue().displayShort()));
		newValueColumn.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().newValue().displayShort()));
		
		changeTableView.setItems(FXCollections.observableList(ChangesManager.getInstance().getAllChanges()));
		changeTableView.getSelectionModel().selectedItemProperty().addListener(this::itemSelected);
	}

	private void itemSelected(Object observable, Change previousValue, Change value) {
		if (value != null) {
			oldValueDetails.getChildren().setAll(new Text(value.oldValue().displayFull()));
			newValueDetails.getChildren().setAll(new Text(value.newValue().displayFull()));
		} else {
			oldValueDetails.getChildren().clear();
			newValueDetails.getChildren().clear();
		}
	}
}
