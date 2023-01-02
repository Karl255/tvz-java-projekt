package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.util.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.util.SemesterStringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class MainScreenController {
	private final int TEMP_TIMETABLE_YEAR = 2022;
	private final LocalDate TEMP_TIMETABLE_START = LocalDate.of(2022, 10, 3);
	private final int TEMP_TIMETABLE_DAYS = 5;

	@FXML private ComboBox<Department> departmentComboBox;
	@FXML private ComboBox<Semester> semesterComboBox;

	@FXML private TimetableDay monday;
	@FXML private TimetableDay tuesday;
	@FXML private TimetableDay wednesday;
	@FXML private TimetableDay thursday;
	@FXML private TimetableDay friday;
	private final TimetableDay[] timetableDays;

	@FXML private Label mondayHolidayText;
	@FXML private Label tuesdayHolidayText;
	@FXML private Label wednesdayHolidayText;
	@FXML private Label thursdayHolidayText;
	@FXML private Label fridayHolidayText;
	@FXML private Label detailsLabel;
	private final Label[] holidayTexts;

	public MainScreenController() {
		timetableDays = new TimetableDay[5];
		holidayTexts = new Label[5];
	}

	@FXML
	private void initialize() {
		timetableDays[0] = monday;
		timetableDays[1] = tuesday;
		timetableDays[2] = wednesday;
		timetableDays[3] = thursday;
		timetableDays[4] = friday;

		holidayTexts[0] = mondayHolidayText;
		holidayTexts[1] = tuesdayHolidayText;
		holidayTexts[2] = wednesdayHolidayText;
		holidayTexts[3] = thursdayHolidayText;
		holidayTexts[4] = fridayHolidayText;

		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());

		var user = Application.getUserManager().getUser();
		var scheduleSource = Application.getScheduleSource();
		
		departmentComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableDepartments()));

		// loading only default department
		if (user.defaultDepartmentCode() != null) {
			if (applyDefaultDepartment(user.defaultDepartmentCode())) {
				semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(user.defaultDepartmentCode(), 2022)));
			}
		}
	}

	@FXML
	private void loadDefaults() {
		var user = Application.getUserManager().getUser();
		var scheduleSource = Application.getScheduleSource();

		if (user.defaultSemester() != null) {
			var sem = user.defaultSemester();
			getTimetable(sem.subdepartment(), sem.semester(), TEMP_TIMETABLE_YEAR, TEMP_TIMETABLE_START, TEMP_TIMETABLE_DAYS);
		}

		if (user.defaultDepartmentCode() != null) {
			if (applyDefaultDepartment(user.defaultDepartmentCode())) {
				semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(user.defaultDepartmentCode(), 2022)));

				if (user.defaultSemester() != null) {
					applyDefaultSemester(user.defaultSemester());
				}
			}
		}
	}

	@FXML
	private void saveDefaults() {
		var department = departmentComboBox.getSelectionModel().getSelectedItem();
		var semester = semesterComboBox.getSelectionModel().getSelectedItem();

		Application.getUserManager().updateLoggedInSettings(department != null ? department.code() : null, semester);
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
		var scheduleSource = Application.getScheduleSource();
		var department = departmentComboBox.getSelectionModel().getSelectedItem();

		if (department != null) {
			semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(department.code(), 2022)));
		}
	}

	@FXML
	private void selectSemester() {
		var semester = semesterComboBox.getSelectionModel().getSelectedItem();

		if (semester != null) {
			getTimetable(semester.subdepartment(), semester.semester(), TEMP_TIMETABLE_YEAR, TEMP_TIMETABLE_START, TEMP_TIMETABLE_DAYS);
		}
	}

	private void getTimetable(String subdepartment, int semester, int year, LocalDate start, int days) {
		var scheduleSource = Application.getScheduleSource();
		var timetable = scheduleSource.getTimetable(subdepartment, semester, year, start, days);

		var itemsByDate = timetable.scheduleItems().stream()
			.collect(Collectors.groupingBy(ScheduleItem::date));

		for (int i = 0; i < 5; i++) {
			var todaysDate = start.plusDays(i);

			var holiday = timetable.holidays()
				.stream()
				.filter(h -> h.date().equals(todaysDate))
				.findFirst();

			if (holiday.isPresent()) {
				holidayTexts[i].setText(holiday.get().title());
			} else if (itemsByDate.containsKey(todaysDate)) {
				holidayTexts[i].setText("");
				timetableDays[i].setItems(itemsByDate.get(todaysDate));
			}
		}
	}

	@FXML
	private void showItemDetails(MouseEvent e) {
		if (e.getSource() instanceof TimetableItem timetableItem) {
			var info = timetableItem.getScheduleItem();
			var sb = new StringBuilder()
				.append(info.getTimestamp()).append('\n')
				.append(info.className()).append('\n')
				.append(info.classType()).append('\n')
				.append(info.classroom()).append('\n')
				.append(info.professor()).append('\n');

			if (info.group() != null) {
				sb.append("Grupa: ").append(info.group());
			}

			if (info.note() != null) {
				sb.append("Napomena: ").append(info.note());
			}

			detailsLabel.setText(sb.toString());
		}
	}
}

