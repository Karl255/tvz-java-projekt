package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.DataNoLongerValidException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EditOverrideController {
	private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	@FXML private TextFlow originalItemTextFlow;
	@FXML private TextFlow selectedReplacementTextFlow;

	@FXML private TableView<ScheduleItem> replacementsTableView;
	@FXML private TableColumn<ScheduleItem, String> replacementStartTimeColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementEndTimeColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementClassTypeColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementClassroomColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementProfessorColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementGroupColumn;
	@FXML private TableColumn<ScheduleItem, String> replacementNoteColumn;

	private ScheduleOverride storedOverride;
	private ScheduleItem original;
	private ScheduleItem selectedReplacement;

	@FXML private Button deleteRowButton;

	@FXML
	private void initialize() {
		original = Application.getOverrideManager().getItemBeingEdited();
		var manager = Application.getOverrideManager();
		ScheduleOverride override;
		
		try {
			override = storedOverride = manager.getOverrideFor(original);

			if (override == null) {
				override = manager.getDefault(original);
			}
		} catch (DataNoLongerValidException e) {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Data invalid", "The data is no longer relevant, please refresh the view.");
			close();
			return;
		}

		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(original)));

		replacementStartTimeColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().start().format(TIME_FORMAT))
		);

		replacementEndTimeColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().end().format(TIME_FORMAT))
		);

		replacementClassTypeColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().classType().toString())
		);

		replacementClassroomColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().classroom())
		);

		replacementProfessorColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().professor())
		);

		replacementGroupColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().group())
		);

		replacementNoteColumn.setCellValueFactory(
			d -> new SimpleStringProperty(d.getValue().note())
		);

		var editableReplacements = new ArrayList<>(override.replacements());
		replacementsTableView.setItems(FXCollections.observableList(editableReplacements));
		replacementsTableView.getSelectionModel().selectedItemProperty().addListener(this::replacementItemSelected);
	}

	private void replacementItemSelected(Object source, ScheduleItem previousValue, ScheduleItem value) {
		selectedReplacement = value;

		if (value != null) {
			selectedReplacementTextFlow.getChildren().setAll(new Text(FXUtil.scheduleItemToString(value)));
			deleteRowButton.setDisable(false);
		} else {
			deleteRowButton.setDisable(true);
			selectedReplacementTextFlow.getChildren().clear();
		}
	}

	@FXML
	private void deleteRowButtonClick() {
		replacementsTableView.getItems()
			.remove(selectedReplacement);
	}

	@FXML
	private void addRowButtonClick() {
		replacementsTableView.getItems()
			.add(original);
	}

	@FXML
	private void closeButtonClick() {
		if (storedOverride != null && storedOverride.areReplacementsEqual(replacementsTableView.getItems())
			|| storedOverride == null && replacementsTableView.getItems().isEmpty()) {
			close();
			return;
		}

		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"There are unsaved changes. Do you want to save them?",
			ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
		);

		alert.setTitle("Unsaved changes");

		var clicked = alert.showAndWait();

		if (clicked.isPresent()) {
			if (clicked.get().equals(ButtonType.YES)) {
				System.out.println("YES");
				save();
				close();
			} else if (clicked.get().equals(ButtonType.NO)) {
				System.out.println("NO");
				close();
			}
		}
	}

	@FXML
	private void deleteAllButtonClick() {
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to delete this override and exit?",
			ButtonType.YES, ButtonType.CANCEL
		);

		alert.setTitle("Confirm deletion");
		alert.show();

		if (alert.getResult() == ButtonType.YES) {
			Application.getOverrideManager().deleteOverride(storedOverride);
		}
	}

	@FXML
	private void saveButtonClick() {
		save();
	}

	private void save() {
		var override = new ScheduleOverride(original, replacementsTableView.getItems());

		Application.getOverrideManager().updateOverride(storedOverride, override);
		storedOverride = override;
	}

	private void close() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}
}
