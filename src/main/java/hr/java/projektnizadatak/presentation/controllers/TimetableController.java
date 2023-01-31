package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.models.TimetableModel;
import hr.java.projektnizadatak.presentation.fx.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.fx.SemesterStringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.format.DateTimeFormatter;

public class TimetableController {
	private static final DateTimeFormatter TIMESTAMP_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	private final int TIMETABLE_DAYS = 5;

	private TimetableModel model;

	@FXML private Button currentWeekButton;

	@FXML private ComboBox<Department> departmentComboBox;
	@FXML private ComboBox<Semester> semesterComboBox;

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
		model = new TimetableModel();
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
		
		currentWeekButton.setText(model.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
		
		/*
		loadDepartments();
		loadDefaults();
		*/
	}

	@FXML
	private void clickLoadDefaults() {
		showNotImplementedAlert();
		//loadDefaults();
	}

	@FXML
	private void clickSaveDefaults() {
		showNotImplementedAlert();
		/*
		var department = departmentComboBox.getValue();
		var semester = semesterComboBox.getValue();

		Application.getUserManager().updateLoggedInSettings(department != null ? department.code() : null, semester);
		*/
	}

	@FXML
	private void departmentSelectedFromCombobox() {
		showNotImplementedAlert();
		/*
		var scheduleSource = Application.getScheduleSource();
		var department = departmentComboBox.getValue();

		if (department != null) {
			semesterComboBox.setItems(FXCollections.observableList(scheduleSource.getAvailableSemesters(department.code(), 2022)));
		}
		*/
	}

	@FXML
	private void semesterSelectedFromCombobox() {
		showNotImplementedAlert();
		/*
		var semester = semesterComboBox.getValue();

		if (semester != null) {
			loadTimetableAsync(semester.subdepartment(), semester.semester(), weekModel.getThisMonday());
		}
		*/
	}

	/*
	private void loadTimetableAsync(String subdepartment, int semester, LocalDate mondayDate) {
		var thread = new Thread(() -> {
			var scheduleSource = Application.getScheduleSource();

			var timetable = ScheduleOverride.applyOverrides(
				scheduleSource.getTimetable(
					subdepartment,
					semester,
					Util.getAcademicYear(mondayDate),
					mondayDate,
					TIMETABLE_DAYS
				),
				Application.getOverrideManager().getAllOverrides(subdepartment, semester));

			Platform.runLater(() -> applyTimetable(timetable));
		});

		thread.setDaemon(true);
		thread.start();
	}

	private void applyTimetable(Timetable timetable) {
		var itemsByWeekday = timetable.scheduleItems().stream()
			.collect(Collectors.groupingBy(ScheduleItem::weekday));

		for (int i = 0; i < 5; i++) {
			var today = timetable.forWeekMonday().plusDays(i);

			var holiday = timetable.holidays()
				.stream()
				.filter(h -> h.date().equals(today))
				.findFirst();

			if (holiday.isPresent()) {
				holidayTexts[i].setText(holiday.get().title());
				timetableDays[i].setItems(Collections.emptyList());
			} else {
				holidayTexts[i].setText("");
				timetableDays[i].setItems(itemsByWeekday.getOrDefault(today.getDayOfWeek(), Collections.emptyList()));
			}
		}

		loadedTimetable = timetable;
	}
	*/

	@FXML
	private void setToCurrentWeek() {
		model.switchToCurrentWeek();
		currentWeekButton.setText(model.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void previousWeek() {
		model.previousWeek();
		currentWeekButton.setText(model.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void nextWeek() {
		model.nextWeek();
		currentWeekButton.setText(model.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void showItemDetails(MouseEvent e) {
		showNotImplementedAlert();
		/*
		if (e.getSource() instanceof TimetableItem timetableItem) {
			selectedItem = timetableItem.getScheduleItem();

			detailsLabel.setText(FXUtil.scheduleItemToString(selectedItem));
			manageOverridesButton.setDisable(false);
		}
		*/
	}

	@FXML
	private void openEditOverride() {
		showNotImplementedAlert();
		/*
		if (selectedItem != null) {
			Application.getOverrideManager().setItemBeingEdited(selectedItem, loadedTimetable.forSubdepartment(), loadedTimetable.forSemester());
			Application.setScreen(ApplicationScreen.EditOverride);
		}
		*/
	}

	@Deprecated
	private void showNotImplementedAlert() {
		var alert = new Alert(Alert.AlertType.INFORMATION, "not implemented");
		alert.show();
	}
}

