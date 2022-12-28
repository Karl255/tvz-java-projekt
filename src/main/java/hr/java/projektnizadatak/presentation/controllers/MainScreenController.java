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

import java.time.LocalDate;

public class MainScreenController {
	@FXML
	private ComboBox<Department> departmentComboBox;
	
	@FXML
	private ComboBox<Semester> semesterComboBox;
	
	@FXML
	private TextArea calendarOutput;
	
	// TODO: move this to a different place
	private final ScheduleApiSource api = new ScheduleApiSource();
	
	private final int calendarYear = 2022;
	private final LocalDate calendarStart = LocalDate.of(2022, 10, 3);
	private final int caledarDays = 7;
	
	public void initialize() {
		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());
		
		departmentComboBox.setItems(FXCollections.observableList(api.fetchAvailableDepartments()));
		
		User user = Application.getUserManager().getUser();
		
		if (user.defaultSemester() != null) {
			var sem = user.defaultSemester();
			getCalendar(sem.subdepartment(), sem.semester(), calendarYear, calendarStart, caledarDays);
		}
		
		if (user.defaultDepartmentCode() != null) {
			if (applyDefaultDepartment(user.defaultDepartmentCode())) {
				semesterComboBox.setItems(FXCollections.observableList(api.fetchAvailableSemesters(user.defaultDepartmentCode(), 2022)));
				
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
		var department = departmentComboBox.getSelectionModel().getSelectedItem();
		
		if (department != null) {
			semesterComboBox.setItems(FXCollections.observableList(api.fetchAvailableSemesters(department.code(), 2022)));
		}
	}
	
	@FXML
	private void selectSemester() {
		var semester = semesterComboBox.getSelectionModel().getSelectedItem();
		
		if (semester != null) {
			getCalendar(semester.subdepartment(), semester.semester(), calendarYear, calendarStart, caledarDays);
		}
	}
	
	private void getCalendar(String subdepartment, int semester, int year, LocalDate start, int days) {
		var calendar = api.fetchCalendar(subdepartment, semester, year, start, days);
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

