package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.ScheduleItemModel;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import hr.java.projektnizadatak.shared.exceptions.DataNoLongerValidException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.css.converter.StringConverter;
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
import java.util.List;

public class EditOverrideController {
	private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");

	@FXML private TextFlow originalItemTextFlow;

	@FXML private TableView<ScheduleItemModel> replacementsTableView;
	@FXML private TableColumn<ScheduleItemModel, LocalTime> replacementStartTimeColumn;
	@FXML private TableColumn<ScheduleItemModel, LocalTime> replacementEndTimeColumn;
	@FXML private TableColumn<ScheduleItemModel, ClassType> replacementClassTypeColumn;
	@FXML private TableColumn<ScheduleItemModel, String> replacementClassroomColumn;
	@FXML private TableColumn<ScheduleItemModel, String> replacementProfessorColumn;
	@FXML private TableColumn<ScheduleItemModel, String> replacementGroupColumn;
	@FXML private TableColumn<ScheduleItemModel, String> replacementNoteColumn;

	private ScheduleOverride storedOverride;
	private ScheduleItem original;

	@FXML private Button deleteRowButton;
	@FXML private Button duplicateRowButton;

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
		
		var editableReplacements = new ArrayList<>(override
			.replacements()
			.stream()
			.map(ScheduleItemModel::new)
			.toList()
		);

		replacementsTableView.setItems(FXCollections.observableList(editableReplacements));
		replacementsTableView.getSelectionModel().selectedItemProperty().addListener(this::replacementItemSelected);
	}

	private void replacementItemSelected(Object source, ScheduleItemModel previousValue, ScheduleItemModel value) {
		if (value == null) {
			duplicateRowButton.setDisable(true);
			deleteRowButton.setDisable(true);
		} else {
			duplicateRowButton.setDisable(false);
			deleteRowButton.setDisable(false);
		}
	}

	private ScheduleItemModel getSelected() {
		return replacementsTableView.getSelectionModel().getSelectedItem();
	}
	
	@FXML
	private void deleteRowButtonClick() {
		replacementsTableView.getItems()
			.remove(getSelected());
	}

	@FXML
	private void duplicateRowButtonClick() {
		replacementsTableView.getItems()
			.add(getSelected().copy());
	}

	@FXML
	private void addRowButtonClick() {
		replacementsTableView.getItems()
			.add(new ScheduleItemModel(original));
	}

	@FXML
	private void closeButtonClick() {
		var replacements = replacementsTableView
			.getItems()
			.stream()
			.map(ScheduleItemModel::toScheduleItem)
			.toList();

		if (storedOverride == null && replacementsTableView.getItems().isEmpty()
			|| storedOverride != null && storedOverride.areReplacementsEqual(replacements)) {
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
		var replacements = replacementsTableView
			.getItems()
			.stream()
			.map(ScheduleItemModel::toScheduleItem)
			.toList();

		var override = new ScheduleOverride(original, replacements);

		Application.getOverrideManager().updateOverride(storedOverride, override);
		storedOverride = override;
	}

	private void close() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}
}
