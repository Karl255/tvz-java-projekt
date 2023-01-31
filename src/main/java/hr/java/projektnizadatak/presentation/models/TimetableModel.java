package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.Util;
import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.util.WeekSwitcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class TimetableModel {
	private final int timetableDays;
	private final WeekSwitcher week;

	private Department selectedDepartment = null;
	private final ObservableList<Department> departmentList = FXCollections.observableArrayList();
	private final Consumer<Department> selectDepartmentSync;

	private Semester selectedSemester = null;
	private final ObservableList<Semester> semesterList = FXCollections.observableArrayList();
	private final Consumer<Semester> selectSemesterSync;

	private Timetable timetable = null;
	private final Consumer<Timetable> setTimetableSync;

	private ScheduleItem selectedItem = null;

	public TimetableModel(
		int timetableDays,
		Consumer<Department> selectDepartmentSync,
		Consumer<Semester> selectSemesterSync,
		Consumer<Timetable> setTimetableSync
	) {
		this.timetableDays = timetableDays;
		this.selectDepartmentSync = selectDepartmentSync;
		this.selectSemesterSync = selectSemesterSync;
		this.setTimetableSync = setTimetableSync;

		week = new WeekSwitcher(LocalDate.now());
	}

	public void initialize() {
		var thread = new Thread(() -> {
			var api = Application.getScheduleSource();
			var deps = api.getAvailableDepartments();
			departmentList.setAll(deps);

			var user = Application.getUserManager().getUser();
			var firstDep = departmentList.stream()
				.findFirst()
				.orElse(null);

			if (user.defaultDepartmentCode() != null) {
				var dep = departmentList.stream()
					.filter(d -> d.code().equals(user.defaultDepartmentCode()))
					.findFirst()
					.orElse(firstDep);

				selectDepartmentSync.accept(dep);
			} else {
				selectDepartmentSync.accept(firstDep);
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

	// department and semester

	public Department getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(Department department) {
		this.selectedDepartment = department;

		var thread = new Thread(() -> {
			if (department == null) {
				return;
			}

			var sems = Application.getScheduleSource().getAvailableSemesters(
				department.code(),
				Util.getAcademicYear(week.getThisMonday())
			);

			semesterList.setAll(sems);

			var user = Application.getUserManager().getUser();

			if (user.defaultSemester() != null) {
				selectSemesterSync.accept(user.defaultSemester());
			} else {
				selectSemesterSync.accept(
					sems.stream().findFirst().orElse(null)
				);
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

	public Semester getSelectedSemester() {
		return selectedSemester;
	}

	public void setSelectedSemester(Semester semester) {
		this.selectedSemester = semester;

		loadTimetable();
	}

	public ObservableList<Department> getDepartmentList() {
		return departmentList;
	}

	public ObservableList<Semester> getSemesterList() {
		return semesterList;
	}

	// timetable

	public Timetable getTimetable() {
		return timetable;
	}

	public void loadTimetable() {
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

			var overrides = Application.getOverrideManager().getAllOverrides(
				semester.subdepartment(),
				semester.semester()
			);

			timetable = ScheduleOverride.applyOverrides(fetchedTimetable, overrides);
			setTimetableSync.accept(timetable);
		});

		thread.setDaemon(true);
		thread.start();
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
		loadTimetable();
	}

	public void previousWeek() {
		week.setToPreviousWeek();
		loadTimetable();
	}

	public void nextWeek() {
		week.setToNextWeek();
		loadTimetable();
	}

	public String getCurrentWeekTimestamp(DateTimeFormatter format) {
		var monday = week.getThisMonday();

		return "%s - %s".formatted(
			monday.format(format),
			monday.plusDays(4).format(format)
		);
	}
}
