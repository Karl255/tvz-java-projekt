package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.EditOverrideModel;
import hr.java.projektnizadatak.presentation.models.OverrideDataItemModel;
import hr.java.projektnizadatak.shared.Pipe;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EditOverrideController {
	private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");
	private final EditOverrideModel model;

	@FXML private TextFlow originalItemTextFlow;

	@FXML private TableView<OverrideDataItemModel> replacementsTableView;
	@FXML private TableColumn<OverrideDataItemModel, LocalTime> replacementStartTimeColumn;
	@FXML private TableColumn<OverrideDataItemModel, LocalTime> replacementEndTimeColumn;
	@FXML private TableColumn<OverrideDataItemModel, ClassType> replacementClassTypeColumn;
	@FXML private TableColumn<OverrideDataItemModel, String> replacementClassroomColumn;
	@FXML private TableColumn<OverrideDataItemModel, String> replacementProfessorColumn;
	@FXML private TableColumn<OverrideDataItemModel, String> replacementGroupColumn;
	@FXML private TableColumn<OverrideDataItemModel, String> replacementNoteColumn;

	@FXML private Button deleteRowButton;
	@FXML private Button duplicateRowButton;

	public EditOverrideController() {
		model = new EditOverrideModel();
	}

	@FXML
	private void initialize() {
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
		replacementClassTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(ClassType.values()));
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

		try {
			model.initialize();
		} catch (DataStoreException e) {
			FXUtil.showDataStoreExceptionAlert(e);
		}

		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(model.getOriginalScheduleOverride().original())));
	}

	private void replacementItemSelected(Object source, OverrideDataItemModel previousValue, OverrideDataItemModel value) {
		if (value == null) {
			duplicateRowButton.setDisable(true);
			deleteRowButton.setDisable(true);
		} else {
			duplicateRowButton.setDisable(false);
			deleteRowButton.setDisable(false);
		}

		model.setSelected(value);
	}

	@FXML
	private void deleteRowButtonClick() {
		model.deleteSelected();
	}

	@FXML
	private void duplicateRowButtonClick() {
		var selected = model.getSelected();
		if (selected != null) {
			model.addReplacement(selected.copy());
		}
	}

	@FXML
	private void addRowButtonClick() {
		var item = Pipe.apply(model.getOriginalScheduleOverride().original())
			.pipe(OverrideData::fromOriginal)
			.pipe(OverrideDataItemModel::new)
			.value;

		model.addReplacement(item);
	}

	@FXML
	private void closeButtonClick() {
		if (!model.needsToSave()) {
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
				try {
					model.save();
					close();
				} catch (DataStoreException e) {
					FXUtil.showDataStoreExceptionAlert(e);
				}
			} else if (clicked.get().equals(ButtonType.NO)) {
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
		var clicked = alert.showAndWait();

		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			try {
				model.deleteAll();
				close();
			} catch (DataStoreException e) {
				FXUtil.showDataStoreExceptionAlert(e);
			}
		}
	}

	@FXML
	private void saveButtonClick() {
		if (!model.needsToSave()) {
			return;
		}

		var alert = new Alert(
			Alert.AlertType.CONFIRMATION,
			"Are you sure you want to save the changes you made?",
			ButtonType.YES, ButtonType.CANCEL
		);

		alert.setTitle("Confirm save");
		var clicked = alert.showAndWait();

		if (clicked.isPresent() && clicked.get().equals(ButtonType.YES)) {
			try {
				model.save();
			} catch (DataStoreException e) {
				FXUtil.showDataStoreExceptionAlert(e);
			}
		}
	}

	private void close() {
		Application.getOverrideManager().setItemBeingEdited(null, null, 0);
		Application.popScreen();
	}
}
