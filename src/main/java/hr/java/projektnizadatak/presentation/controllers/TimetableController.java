package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.Util;
import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.WeekModel;
import hr.java.projektnizadatak.presentation.fx.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.fx.SemesterStringConverter;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

public class TimetableController {
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	private final int TIMETABLE_DAYS = 5;

	private final WeekModel weekModel;
	@FXML private Button currentWeekButton;

	@FXML private ComboBox<Department> departmentComboBox;
	@FXML private ComboBox<Semester> semesterComboBox;

	private ScheduleItem selectedItem = null;
	@FXML private Button manageOverridesButton;

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

	public TimetableController() {
		this.timetableDays = new TimetableDay[5];
		this.holidayTexts = new Label[5];
		this.weekModel = new WeekModel();
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

		updateCurrentWeekButton();

		departmentComboBox.setConverter(new DepartmentStringConverter());
		semesterComboBox.setConverter(new SemesterStringConverter());

		var user = Application.getUserManager().getUser();
		var scheduleSource = Application.getScheduleSource();

		departmentComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableDepartments()));

		// loading only default department
		if (user.defaultDepartmentCode() != null) {
			if (chooseDepartment(user.defaultDepartmentCode())) {
				semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(user.defaultDepartmentCode(), 2022)));

				if (user.defaultSemester() != null) {
					chooseSemester(user.defaultSemester());
				}
			}
		}
	}

	@FXML
	private void loadDefaults() {
		var user = Application.getUserManager().getUser();
		var scheduleSource = Application.getScheduleSource();

		if (user.defaultDepartmentCode() != null && user.defaultSemester() != null) {
			var sem = user.defaultSemester();
			getTimetable(sem.subdepartment(), sem.semester(), weekModel.getThisMonday());
		}

		if (user.defaultDepartmentCode() != null && chooseDepartment(user.defaultDepartmentCode())) {
			semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(user.defaultDepartmentCode(), 2022)));

			if (user.defaultSemester() != null) {
				chooseSemester(user.defaultSemester());
			}
		}
	}

	@FXML
	private void saveDefaults() {
		var department = departmentComboBox.getValue();
		var semester = semesterComboBox.getValue();

		Application.getUserManager().updateLoggedInSettings(department != null ? department.code() : null, semester);
	}

	private boolean chooseDepartment(String departmentCode) {
		var department = departmentComboBox.getItems().stream()
			.filter(d -> d.code().equals(departmentCode))
			.findFirst();

		if (department.isPresent()) {
			departmentComboBox.setValue(department.get());
		}

		return department.isPresent();
	}

	private boolean chooseSemester(Semester semester) {
		var foundSem = semesterComboBox.getItems().stream()
			.anyMatch(s -> s.equals(semester));

		if (foundSem) {
			semesterComboBox.setValue(semester);
			return true;
		}

		return false;
	}

	@FXML
	private void departmentSelectedFromCombobox() {
		var scheduleSource = Application.getScheduleSource();
		var department = departmentComboBox.getValue();

		if (department != null) {
			semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(department.code(), 2022)));
		}
	}

	@FXML
	private void semesterSelectedFromCombobox() {
		var semester = semesterComboBox.getValue();

		if (semester != null) {
			getTimetable(semester.subdepartment(), semester.semester(), weekModel.getThisMonday());
		}
	}

	private void getTimetable(String subdepartment, int semester, LocalDate monday) {
		var scheduleSource = Application.getScheduleSource();

		var timetable = scheduleSource.getTimetable(
			subdepartment,
			semester,
			Util.getAcademicYear(monday),
			monday,
			TIMETABLE_DAYS);
		
		var overrides = Application.getOverrideManager().getAllOverrides();

		timetable = ScheduleOverride.applyOverrides(timetable, overrides);
		
		var itemsByDate = timetable.scheduleItems().stream()
			.collect(Collectors.groupingBy(ScheduleItem::date));

		for (int i = 0; i < 5; i++) {
			var todaysDate = monday.plusDays(i);

			var holiday = timetable.holidays()
				.stream()
				.filter(h -> h.date().equals(todaysDate))
				.findFirst();

			if (holiday.isPresent()) {
				holidayTexts[i].setText(holiday.get().title());
				timetableDays[i].setItems(Collections.emptyList());
			} else {
				holidayTexts[i].setText("");
				timetableDays[i].setItems(itemsByDate.getOrDefault(todaysDate, Collections.emptyList()));
			}
		}
	}

	@FXML
	private void setCurrentWeek() {
		weekModel.setCurrentWeek();
		updateCurrentWeek();
	}

	@FXML
	private void previousWeek() {
		weekModel.setPreviousWeek();
		updateCurrentWeek();
	}

	@FXML
	private void nextWeek() {
		weekModel.setNextWeek();
		updateCurrentWeek();
	}

	private void updateCurrentWeek() {
		updateCurrentWeekButton();

		var department = departmentComboBox.getValue();
		var semester = semesterComboBox.getValue();

		if (department != null && semester != null) {
			getTimetable(semester.subdepartment(), semester.semester(), weekModel.getThisMonday());
		}
	}

	private void updateCurrentWeekButton() {
		currentWeekButton.setText(weekModel.toTimespan(5, DATE_FORMAT));
	}

	@FXML
	private void showItemDetails(MouseEvent e) {
		if (e.getSource() instanceof TimetableItem timetableItem) {
			selectedItem = timetableItem.getScheduleItem();
			
			detailsLabel.setText(FXUtil.scheduleItemToString(selectedItem));
			manageOverridesButton.setDisable(false);
		}
	}
	
	@FXML
	private void openEditOverride() {
		if (selectedItem != null) {
			Application.getOverrideManager().setItemBeingEdited(selectedItem);
			Application.setScreen(ApplicationScreen.EditOverride);
		}
	}
}

