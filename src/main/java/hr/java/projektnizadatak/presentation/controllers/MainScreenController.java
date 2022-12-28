package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.data.ScheduleApiSource;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.util.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.util.SemesterStringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MainScreenController {
	@FXML
	private ComboBox<Department> departmentComboBox;
	
	@FXML
	private ComboBox<Semester> semesterComboBox;
	
	@FXML
	private TextArea depsOutput;
	@FXML
	private TextArea semsOutput;
	@FXML
	private TextArea calendarOutput;
	
	// TODO: move this to a different place
	private final ScheduleApiSource api = new ScheduleApiSource();
	
	public void initialize() {
		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());
		
		departmentComboBox.setItems(FXCollections.observableList(api.fetchAvailableDepartments()));
		
		User user = Application.getUserManager().getUser();
		
		if (user.defaultSemester() != null) {
			getCalendar();
		}
		
		if (user.defaultDepartmentCode() != null) {
			if (applyDefaultDepartment(user.defaultDepartmentCode())) {
				semesterComboBox.setItems(FXCollections.observableList(api.fetchAvailableSemesters()));
				
				if (user.defaultSemester() != null) {
					applyDefaultSemester(user.defaultSemester());
				}
			}
		}
	}
	
	private boolean applyDefaultDepartment(String departmentCode) {
		var department = departmentComboBox.getItems().stream()
			.filter(d -> d.code().equals(departmentCode))
			.findFirst();
		
		if (department.isPresent()) {
			departmentComboBox.getSelectionModel().select(department.get());
		}
		
		return department.isPresent();
	}
	
	private void applyDefaultSemester(Semester semester) {
		semesterComboBox.getSelectionModel().select(semester);
	}
	
	@FXML
	private void selectDepartment() {
		// fetch new semesters
	}
	
	@FXML
	private void selectSemester() {
		
	}
	
	private void getCalendar() {
		var calendar = api.fetchCalendar();
		var sb = new StringBuilder();
		
		for (var scheduleItem : calendar.scheduleItems()) {
			sb.append(scheduleItem.title())
				.append('\n');
		}
		
		sb.append('\n');
		
		for (var holiday : calendar.holidays()) {
			sb.append(holiday.title())
				.append('\n');
		}
		
		calendarOutput.setText(sb.toString());
	}
}

