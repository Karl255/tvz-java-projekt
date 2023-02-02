package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.models.TimetableModel;
import hr.java.projektnizadatak.presentation.fx.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.fx.SemesterStringConverter;
import hr.java.projektnizadatak.presentation.views.ApplicationScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

public class TimetableController {
	private static final DateTimeFormatter TIMESTAMP_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	private final int TIMETABLE_DAYS = 5;

	private final TimetableModel model;

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
		model = new TimetableModel(
			TIMETABLE_DAYS,
			this::selectDepartment,
			this::selectSemester,
			this::setTimetableSync
		);

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
		departmentComboBox.setItems(model.getDepartmentList());
		semesterComboBox.setConverter(new SemesterStringConverter());
		semesterComboBox.setItems(model.getSemesterList());

		currentWeekButton.setText(model.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));

		model.initialize();
		Application.setOnWindowResizeHandler(this::onWindowResize);
	}
	
	private void onWindowResize(double w, double h) {
		for (var day : timetableDays) {
			day.repositionItems();
		}
	}

	@FXML
	private void clickLoadDefaults() {
		model.loadUserDefaultChoices();
	}

	@FXML
	private void clickSaveDefaults() {
		model.saveUserDefaultChoices();
	}

	@FXML
	private void departmentSelected() {
		model.setSelectedDepartment(departmentComboBox.getValue());
	}

	@FXML
	private void semesterSelected() {
		model.setSelectedSemester(semesterComboBox.getValue());
	}

	private void selectDepartment(Department department) {
		departmentComboBox.setValue(department);
	}

	private void selectSemester(Semester semester) {
		semesterComboBox.setValue(semester);
	}

	private void setTimetableSync(Timetable timetable) {
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
	}

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
		if (e.getSource() instanceof TimetableItem timetableItem) {
			var item = timetableItem.getScheduleItem();

			detailsLabel.setText(FXUtil.scheduleItemToString(item));
			manageOverridesButton.setDisable(false);
			model.setSelectedItem(item);
		}
	}

	@FXML
	private void openEditOverride() {
		var item = model.getSelectedItem();

		if (item != null) {
			Application.getOverrideManager().setItemBeingEdited(item, model.getTimetable().forSubdepartment(), model.getTimetable().forSemester());
			Application.pushScreen(ApplicationScreen.EditOverride);
		}
	}
}

