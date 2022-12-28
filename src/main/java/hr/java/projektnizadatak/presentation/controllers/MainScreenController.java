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
		var deps = api.fetchAvailableDepartments();
		var sb = new StringBuilder();
		
		for (var dep : deps) {
			sb.append(dep.code())
				.append('\n');
		}
		
		depsOutput.setText(sb.toString());
	}
	
	@FXML
	private void fetchSems() {
		var sems = api.fetchAvailableSemesters();
		var sb = new StringBuilder();

		for (var sem : sems) {
			sb.append(sem.subdepartment())
				.append(" - ")
				.append(sem.semester())
				.append('\n');
		}
		
		semsOutput.setText(sb.toString());
	}
	
	@FXML
	private void fetchCalendar() {
		var events = api.fetchCalendar();
		var sb = new StringBuilder();

		for (var event : events) {
			sb.append(event.title())
				.append("\n\n");
		}
		
		calendarOutput.setText(sb.toString());
	}
}
