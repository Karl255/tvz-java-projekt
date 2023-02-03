package hr.java.projektnizadatak.presentation.views;

import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.controllers.TimetableController;
import hr.java.projektnizadatak.presentation.fx.DepartmentStringConverter;
import hr.java.projektnizadatak.presentation.fx.SemesterStringConverter;
import hr.java.projektnizadatak.presentation.ApplicationScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

public class TimetableView {
	private static final DateTimeFormatter TIMESTAMP_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	private final int TIMETABLE_DAYS = 5;

	private final TimetableController controller;

	@FXML private Button currentWeekButton;

	@FXML private ComboBox<Department> departmentComboBox;
	@FXML private ComboBox<Semester> semesterComboBox;

	@FXML private Button manageOverridesButton;

	@FXML private TimetableDayView monday;
	@FXML private TimetableDayView tuesday;
	@FXML private TimetableDayView wednesday;
	@FXML private TimetableDayView thursday;
	@FXML private TimetableDayView friday;
	private final TimetableDayView[] timetableDays;

	@FXML private Label mondayHolidayText;
	@FXML private Label tuesdayHolidayText;
	@FXML private Label wednesdayHolidayText;
	@FXML private Label thursdayHolidayText;
	@FXML private Label fridayHolidayText;
	@FXML private Label detailsLabel;
	private final Label[] holidayTexts;

	public TimetableView() {
		controller = new TimetableController(
			TIMETABLE_DAYS,
			this::selectDepartment,
			this::selectSemester,
			this::setTimetableSync
		);

		timetableDays = new TimetableDayView[5];
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
		departmentComboBox.setItems(controller.getDepartmentList());
		semesterComboBox.setConverter(new SemesterStringConverter());
		semesterComboBox.setItems(controller.getSemesterList());

		currentWeekButton.setText(controller.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));

		controller.initialize();
		Application.setOnWindowResizeHandler(this::onWindowResize);
	}
	
	private void onWindowResize(double w, double h) {
		for (var day : timetableDays) {
			day.repositionItems();
		}
	}

	@FXML
	private void clickLoadDefaults() {
		controller.loadUserDefaultChoices();
	}

	@FXML
	private void clickSaveDefaults() {
		controller.saveUserDefaultChoices();
	}

	@FXML
	private void departmentSelected() {
		controller.setSelectedDepartment(departmentComboBox.getValue());
	}

	@FXML
	private void semesterSelected() {
		controller.setSelectedSemester(semesterComboBox.getValue());
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
		controller.switchToCurrentWeek();
		currentWeekButton.setText(controller.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void previousWeek() {
		controller.previousWeek();
		currentWeekButton.setText(controller.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void nextWeek() {
		controller.nextWeek();
		currentWeekButton.setText(controller.getCurrentWeekTimestamp(TIMESTAMP_DATE_FORMAT));
	}

	@FXML
	private void showItemDetails(MouseEvent e) {
		if (e.getSource() instanceof TimetableItemModel timetableItem) {
			var item = timetableItem.getScheduleItem();

			detailsLabel.setText(FXUtil.scheduleItemToString(item));
			manageOverridesButton.setDisable(false);
			controller.setSelectedItem(item);
		}
	}

	@FXML
	private void openEditOverride() {
		var item = controller.getSelectedItem();

		if (item != null) {
			Application.getOverrideManager().setItemBeingEdited(item, controller.getTimetable().forSubdepartment(), controller.getTimetable().forSemester());
			Application.pushScreen(ApplicationScreen.EditOverride);
		}
	}
}

