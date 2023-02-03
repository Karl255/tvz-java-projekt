package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.controllers.EditOverrideController;
import hr.java.projektnizadatak.presentation.models.OverrideDataModel;
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

public class EditOverrideView {
	private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm");
	private final EditOverrideController controller;

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

	public EditOverrideView() {
		controller = new EditOverrideController();
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

		replacementsTableView.setItems(controller.getReplacements());
		replacementsTableView.getSelectionModel().selectedItemProperty().addListener(this::replacementItemSelected);

		try {
			controller.initialize();
		} catch (DataStoreException e) {
			FXUtil.showDataStoreExceptionAlert(e);
		}

		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(controller.getOriginalScheduleOverride().original())));
	}

	private void replacementItemSelected(Object source, OverrideDataModel previousValue, OverrideDataModel value) {
		if (value == null) {
			duplicateRowButton.setDisable(true);
			deleteRowButton.setDisable(true);
		} else {
			duplicateRowButton.setDisable(false);
			deleteRowButton.setDisable(false);
		}

		controller.setSelected(value);
	}

	@FXML
	private void deleteRowButtonClick() {
		controller.deleteSelected();
	}

	@FXML
	private void duplicateRowButtonClick() {
		var selected = controller.getSelected();
		if (selected != null) {
			controller.addReplacement(selected.copy());
		}
	}

	@FXML
	private void addRowButtonClick() {
		var item = Pipe.apply(controller.getOriginalScheduleOverride().original())
			.pipe(OverrideData::fromOriginal)
			.pipe(OverrideDataModel::new)
			.value;

		controller.addReplacement(item);
	}

	@FXML
	private void closeButtonClick() {
		if (!controller.needsToSave()) {
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
					controller.save();
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
				controller.deleteAll();
				close();
			} catch (DataStoreException e) {
				FXUtil.showDataStoreExceptionAlert(e);
			}
		}
	}

	@FXML
	private void saveButtonClick() {
		if (!controller.needsToSave()) {
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
				controller.save();
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
