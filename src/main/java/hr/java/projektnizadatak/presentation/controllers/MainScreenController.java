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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainScreenController {
	@FXML
	private ComboBox<Department> departmentComboBox;
	
	@FXML
	private ComboBox<Semester> semesterComboBox;
	
	@FXML private CalendarDay monday;
	@FXML private CalendarDay tuesday;
	@FXML private CalendarDay wednesday;
	@FXML private CalendarDay thursday;
	@FXML private CalendarDay friday;
	private final CalendarDay[] calendarDays;
	
	@FXML private Label mondayHolidayText;
	@FXML private Label tuesdayHolidayText;
	@FXML private Label wednesdayHolidayText;
	@FXML private Label thursdayHolidayText;
	@FXML private Label fridayHolidayText;
	private final Label[] holidayTexts;
	
	// TODO: move this to a different place
	private final ScheduleApiSource api = new ScheduleApiSource();
	
	private final int TEMP_CALENDAR_YEAR = 2022;
	private final LocalDate TEMP_CALENDAR_START = LocalDate.of(2022, 10, 3);
	private final int TEMP_CALENDAR_DAYS = 5;

	public MainScreenController() {
		calendarDays = new CalendarDay[5];
		holidayTexts = new Label[5];
	}

	public void initialize() {
		calendarDays[0] = monday;
		calendarDays[1] = tuesday;
		calendarDays[2] = wednesday;
		calendarDays[3] = thursday;
		calendarDays[4] = friday;
		
		holidayTexts[0] = mondayHolidayText;
		holidayTexts[1] = tuesdayHolidayText;
		holidayTexts[2] = wednesdayHolidayText;
		holidayTexts[3] = thursdayHolidayText;
		holidayTexts[4] = fridayHolidayText;

		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());
		
		departmentComboBox.setItems(FXCollections.observableList(api.fetchAvailableDepartments()));
		
		// TODO: this should happen after initialization
		User user = Application.getUserManager().getUser();
		
		if (user.defaultSemester() != null) {
			var sem = user.defaultSemester();
			//getCalendar(sem.subdepartment(), sem.semester(), TEMP_CALENDAR_YEAR, TEMP_CALENDAR_START, TEMP_CALENDAR_DAYS);
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
			getCalendar(semester.subdepartment(), semester.semester(), TEMP_CALENDAR_YEAR, TEMP_CALENDAR_START, TEMP_CALENDAR_DAYS);
		}
	}
	
	private void getCalendar(String subdepartment, int semester, int year, LocalDate start, int days) {
		var calendar = api.fetchCalendar(subdepartment, semester, year, start, days);
		
		var itemsByDate = calendar.scheduleItems().stream()
			.collect(Collectors.groupingBy(e -> e.start().toLocalDate()));
		
		for (int i = 0; i < 5; i++) {
			var todaysDate = start.plusDays(i);
			
			var holiday = calendar.holidays()
				.stream()
				.filter(h -> h.date().equals(todaysDate))
				.findFirst();
			
			if (holiday.isPresent()) {
				holidayTexts[i].setText(holiday.get().title());
			} else if (itemsByDate.containsKey(todaysDate)) {
				holidayTexts[i].setText("");
				calendarDays[i].setItems(itemsByDate.get(todaysDate));
			}
		}
	}
	
	@FXML
	private void saveSettings() {
		var department = departmentComboBox.getSelectionModel().getSelectedItem();
		var semester = semesterComboBox.getSelectionModel().getSelectedItem();
		
		Application.getUserManager().updateLoggedInSettings(department != null ? department.code() : null, semester);
	}
}

