package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.data.ScheduleApiSource;
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
	
	private final ScheduleApiSource api = new ScheduleApiSource();
	
	public void initialize() {
		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());
		
		departmentComboBox.setItems(FXCollections.observableList(api.fetchAvailableDepartments()));
		
		// TODO: apply user settings
		// TODO: select department in combobox
		
		// TODO: only if departmentModel has department selected
		semesterComboBox.setItems(FXCollections.observableList(api.fetchAvailableSemesters()));
		// TODO: select semester in combobox
	}
	
	@FXML
	private void selectDepartment() {
		// fetch new semesters
	}
	
	@FXML
	private void selectSemester() {
		
	}
}

