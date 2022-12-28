package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.data.ScheduleApiSource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MainScreenController {
	@FXML
	private TextArea depsOutput;
	@FXML
	private TextArea semsOutput;
	@FXML
	private TextArea calendarOutput;
	
	private ScheduleApiSource api = new ScheduleApiSource();
	
	@FXML
	private void fetchDeps() {
		depsOutput.setText(api.fetchAvailableDepartments());
	}
	
	@FXML
	private void fetchSems() {
		semsOutput.setText(api.fetchAvailableSemesters());
	}
	
	@FXML
	private void fetchCalendar() {
		calendarOutput.setText(api.fetchCalendar());
	}
}
