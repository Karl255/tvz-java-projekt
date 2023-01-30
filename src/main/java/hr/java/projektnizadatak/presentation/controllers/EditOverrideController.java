package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.EditOverrideModel;
import hr.java.projektnizadatak.presentation.models.OverrideDataModel;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class EditOverrideController {
	private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");
	private final EditOverrideModel model;
	
	@FXML private TextFlow originalItemTextFlow;

	@FXML private TableView<OverrideDataModel> replacementsTableView;
	@FXML private TableColumn<OverrideDataModel, LocalTime> replacementStartTimeColumn;
	@FXML private TableColumn<OverrideDataModel, LocalTime> replacementEndTimeColumn;
	@FXML private TableColumn<OverrideDataModel, ClassType> replacementClassTypeColumn;
	@FXML private TableColumn<OverrideDataModel, String> replacementClassroomColumn;
	@FXML private TableColumn<OverrideDataModel, String> replacementProfessorColumn;
	@FXML private TableColumn<OverrideDataModel, String> replacementGroupColumn;
	@FXML private TableColumn<OverrideDataModel, String> replacementNoteColumn;

	@FXML private Button deleteRowButton;
	@FXML private Button duplicateRowButton;

	public EditOverrideController() {
		model = new EditOverrideModel();
	}

	@FXML
	private void initialize() {
		model.initialize();
		
		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(model.getOriginalScheduleOverride().original())));

		replacementStartTimeColumn.setCellValueFactory(d -> d.getValue().startProperty());
		replacementStartTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(
			new LocalTimeStringConverter(TIME_FORMAT, TIME_FORMAT)
		));
		replacementStartTimeColumn.setOnEditCommit(e -> e.getRowValue().setStart(e.getNewValue()));
		
		replacementEndTimeColumn.setCellValueFactory(d -> d.getValue().endProperty());
		replacementEndTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(
			new LocalTimeStringConverter(TIME_FORMAT, TIME_FORMAT)
		));
		replacementEndTimeColumn.setOnEditCommit(e -> e.getRowValue().setEnd(e.getNewValue()));
		
		replacementClassTypeColumn.setCellValueFactory(d -> d.getValue().classTypeProperty());
		replacementClassTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
			FXCollections.observableList(new ArrayList<>(Arrays.asList(ClassType.values())))
		));
		replacementClassTypeColumn.setOnEditCommit(e -> e.getRowValue().setClassType(e.getNewValue()));
		
		replacementClassroomColumn.setCellValueFactory(d -> d.getValue().classroomProperty());
		replacementClassroomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		replacementClassroomColumn.setOnEditCommit(e -> e.getRowValue().setProfessor(e.getNewValue()));
		
		replacementProfessorColumn.setCellValueFactory(d -> d.getValue().professorProperty());
		replacementProfessorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		replacementProfessorColumn.setOnEditCommit(e -> e.getRowValue().setProfessor(e.getNewValue()));
		
		replacementGroupColumn.setCellValueFactory(d -> d.getValue().groupProperty());
		replacementGroupColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		replacementGroupColumn.setOnEditCommit(e -> e.getRowValue().setGroup(e.getNewValue()));
		
		replacementNoteColumn.setCellValueFactory(d -> d.getValue().noteProperty());
		replacementNoteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		replacementNoteColumn.setOnEditCommit(e -> e.getRowValue().setNote(e.getNewValue()));

		replacementsTableView.setItems(model.getReplacements());
		replacementsTableView.getSelectionModel().selectedItemProperty().addListener(this::replacementItemSelected);
	}

	private void replacementItemSelected(Object source, OverrideDataModel previousValue, OverrideDataModel value) {
		if (value == null) {
			duplicateRowButton.setDisable(true);
			deleteRowButton.setDisable(true);
		} else {
			duplicateRowButton.setDisable(false);
			deleteRowButton.setDisable(false);
		}
	}

	private OverrideDataModel getSelected() {
		return replacementsTableView.getSelectionModel().getSelectedItem();
	}
	
	@FXML
	private void deleteRowButtonClick() {
		// TODO
		showNotImplementedAlert();
	}

	@FXML
	private void duplicateRowButtonClick() {
		// TODO
		showNotImplementedAlert();
	}

	@FXML
	private void addRowButtonClick() {
		// TODO
		showNotImplementedAlert();
	}

	@FXML
	private void closeButtonClick() {
		// TODO
		
		/*
		if (!wereChangesMade()) {
			close();
			return;
		}
		*/

		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"There are unsaved changes. Do you want to save them?",
			ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
		);

		alert.setTitle("Unsaved changes");

		var clicked = alert.showAndWait();

		if (clicked.isPresent()) {
			if (clicked.get().equals(ButtonType.YES)) {
				//save();
				close();
			} else if (clicked.get().equals(ButtonType.NO)) {
				close();
			}
		}
	}

	@FXML
	private void deleteAllButtonClick() {
		// TODO
		showNotImplementedAlert();
		if (true) return;
		
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to delete this override and exit?",
			ButtonType.YES, ButtonType.CANCEL
		);

		alert.setTitle("Confirm deletion");
		var clicked = alert.showAndWait();

		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			//Application.getOverrideManager().deleteOverride(storedOverride);
			close();
		}
	}

	@FXML
	private void saveButtonClick() {
		// TODO
		showNotImplementedAlert();
		if (true) return;
		
		/*
		if (!wereChangesMade()) {
			save();
			return;
		}
		*/
		
		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to save the changes you made?",
			ButtonType.YES, ButtonType.CANCEL
		);
		
		alert.setTitle("Confirm save");
		var clicked = alert.showAndWait();
		
		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			//save();
		}
	}

	private void close() {
		Application.getOverrideManager().setItemBeingEdited(null);
		Application.setScreen(ApplicationScreen.Timetable);
	}
	
	@Deprecated
	private void showNotImplementedAlert() {
		var alert = new Alert(Alert.AlertType.INFORMATION, "Not implemented");
		alert.show();
	}
}
