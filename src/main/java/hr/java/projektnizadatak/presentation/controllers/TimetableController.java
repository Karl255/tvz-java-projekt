package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.Util;
import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import hr.java.projektnizadatak.presentation.util.WeekSwitcher;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class TimetableController {
	private final int timetableDays;
	private final WeekSwitcher week;

	private Department selectedDepartment = null;
	private final ObservableList<Department> departmentList = FXCollections.observableArrayList();
	private final Consumer<Department> selectDepartment;

	private Semester selectedSemester = null;
	private final ObservableList<Semester> semesterList = FXCollections.observableArrayList();
	private final Consumer<Semester> selectSemester;

	private Timetable timetable = null;
	private final Consumer<Timetable> setTimetable;

	private ScheduleItem selectedItem = null;

	public TimetableController(
		int timetableDays,
		Consumer<Department> selectDepartment,
		Consumer<Semester> selectSemester,
		Consumer<Timetable> setTimetable
	) {
		this.timetableDays = timetableDays;
		this.selectDepartment = selectDepartment;
		this.selectSemester = selectSemester;
		this.setTimetable = setTimetable;

		week = new WeekSwitcher(LocalDate.now());
	}

	public void initialize() {
		loadDepartmentsAsync();
	}

	// department

	public Thread loadDepartmentsAsync() {
		var thread = new Thread(() -> {
			var api = Application.getScheduleSource();
			var deps = api.getAvailableDepartments();

			Platform.runLater(() -> {
				departmentList.setAll(deps);
				selectDefaultDepartment();
			});
		});

		thread.setDaemon(true);
		thread.start();

		return thread;
	}

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department department) {
		this.selectedDepartment = department;

		loadSemestersAsync();
	}

	private void selectDefaultDepartment() {
		var user = Application.getUserManager().getLoggedInUser();
		var firstDep = departmentList.stream()
			.findFirst()
			.orElse(null);

		if (user.defaultDepartmentCode() != null) {
			var dep = departmentList.stream()
				.filter(d -> d.code().equals(user.defaultDepartmentCode()))
				.findFirst()
				.orElse(firstDep);

			selectDepartment.accept(dep);
		} else {
			selectDepartment.accept(firstDep);
		}
	}

	// semester
	
	public Thread loadSemestersAsync() {
		var thread = new Thread(() -> {
			var department = selectedDepartment;

			if (department == null) {
				return;
			}

			var sems = Application.getScheduleSource().getAvailableSemesters(
				department.code(),
				Util.getAcademicYear(week.getThisMonday())
			);

			Platform.runLater(() -> {
				semesterList.setAll(sems);
				selectDefaultSemester();
			});
		});

		thread.setDaemon(true);
		thread.start();

		return thread;
	}

	public Semester getSelectedSemester() {
		return selectedSemester;
	}

	public void setSelectedSemester(Semester semester) {
		this.selectedSemester = semester;

		loadTimetableAsync();
	}
	
	private void selectDefaultSemester() {
		var user = Application.getUserManager().getLoggedInUser();
		var firstSem = semesterList.stream()
			.findFirst()
			.orElse(null);
		
		if (user.defaultSemester() != null) {
			var sem = semesterList.stream()
				.filter(s -> s.equals(user.defaultSemester()))
				.findFirst()
				.orElse(firstSem);
				
			selectSemester.accept(sem);
		} else {
			selectSemester.accept(
				semesterList.stream().findFirst().orElse(null)
			);
		}
	}

	public ObservableList<Department> getDepartmentList() {
		return departmentList;
	}

	public ObservableList<Semester> getSemesterList() {
		return semesterList;
	}

	public void loadUserDefaultChoices() {
		selectDepartment.accept(null);
		selectDefaultDepartment();
	}

	public void saveUserDefaultChoices() {
		Application.getUserManager().updateLoggedInSettings(
			selectedDepartment.code(),
			selectedSemester
		);
	}

	// timetable

	public Timetable getTimetable() {
		return timetable;
	}

	public Thread loadTimetableAsync() {
		var user = Application.getUserManager().getLoggedInUser();
		
		var thread = new Thread(() -> {
			var semester = selectedSemester;

			if (semester == null) {
				return;
			}

			var fetchedTimetable = Application.getScheduleSource().getTimetable(
				semester.subdepartment(),
				semester.semester(),
				Util.getAcademicYear(week.getThisMonday()),
				week.getThisMonday(),
				timetableDays
			);

			try {
				List<ScheduleOverride> overrides = Application.getOverrideManager().getAllUserOverridesFor(
					user.username(),
					semester.subdepartment(),
					semester.semester()
				);

				Platform.runLater(() -> {
					timetable = ScheduleOverride.applyOverrides(fetchedTimetable, overrides);
					setTimetable.accept(timetable);
				});
			} catch (DataStoreException e) {
				Platform.runLater(() -> {
					FXUtil.showDataStoreExceptionAlert(e);
				});
			}
		});

		thread.setDaemon(true);
		thread.start();

		return thread;
	}

	// selected item

	public ScheduleItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ScheduleItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	// week-related stuff

	public void switchToCurrentWeek() {
		week.setWeek(LocalDate.now());
		loadTimetableAsync();
	}

	public void previousWeek() {
		week.setToPreviousWeek();
		loadTimetableAsync();
	}

	public void nextWeek() {
		week.setToNextWeek();
		loadTimetableAsync();
	}

	public String getCurrentWeekTimestamp(DateTimeFormatter format) {
		var monday = week.getThisMonday();

		return "%s - %s".formatted(
			monday.format(format),
			monday.plusDays(4).format(format)
		);
	}
}
