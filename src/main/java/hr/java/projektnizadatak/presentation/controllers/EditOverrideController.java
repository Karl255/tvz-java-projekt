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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;

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
	private ScheduleOverride override;
	
	@FXML
	private void initialize() {
		var selectedItem = Application.getOverrideManager().getItemBeingEdited();
		var manager = Application.getOverrideManager();
		
		try {
			storedOverride = override = manager.getOverrideFor(selectedItem);
			
			if (override == null) {
				override = manager.getDefault(selectedItem);
			}
		} catch (DataNoLongerValidException e) {
			FXUtil.showAlert(Alert.AlertType.ERROR, "Data invalid", "The data is no longer relevant, please refresh the view.");
			close();
		}
		
		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(override.original())));
		
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
		
		replacementsTableView.setItems(FXCollections.observableList(override.replacements()));
	}
	
	@FXML
	private void closeButtonClick() {
		if (override.equals(storedOverride)) {
			close();
		}
		
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"There are unsaved changes. Do you want to save them?",
			ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
		);
		
		alert.setTitle("Unsaved changes");
		alert.show();

		var clicked = alert.getResult();
		
		if (clicked == ButtonType.YES) {
			save();
			close();
		} else if (clicked == ButtonType.NO) {
			close();
		}
	}
	
	@FXML
	private void deleteButtonClick() {
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to delete this override?",
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
		Application.getOverrideManager().updateOverride(storedOverride, override);
		storedOverride = override;
	}
	
	private void close() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}
}
