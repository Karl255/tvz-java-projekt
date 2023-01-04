package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class EditOverrideController {
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
	
	@FXML
	private void initialize() {
		var selectedItem = Application.getOverrideManager().getItemBeingEdited();
		
		originalItemTextFlow.getChildren().add(new Text(FXUtil.scheduleItemToString(selectedItem)));
	}
	
	@FXML
	private void close() {
		Application.setScreen(ApplicationScreen.MainScreen);
	}
}
