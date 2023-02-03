package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.ChangesManager;
import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.Recordable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;

public class ChangesBrowserView {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

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
		oldValueColumn.setCellValueFactory(d -> getDisplayShort(d.getValue().oldValue()));
		newValueColumn.setCellValueFactory(d -> getDisplayShort(d.getValue().newValue()));

		changeTableView.setItems(FXCollections.observableList(ChangesManager.getInstance().getAllChanges()));
		changeTableView.getSelectionModel().selectedItemProperty().addListener(this::itemSelected);
	}

	private void itemSelected(Object observable, Change previousValue, Change value) {
		if (value != null) {
			oldValueDetails.getChildren().setAll(getDisplayFull(value.oldValue()));
			newValueDetails.getChildren().setAll(getDisplayFull(value.newValue()));
		} else {
			oldValueDetails.getChildren().clear();
			newValueDetails.getChildren().clear();
		}
	}

	private static ObservableValue<String> getDisplayShort(Recordable value) {
		return new SimpleStringProperty(value != null
			? value.displayShort()
			: ""
		);
	}

	private static Text getDisplayFull(Recordable value) {
		return new Text(value != null
			? value.displayFull()
			: ""
		);
	}
}
