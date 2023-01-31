package hr.java.projektnizadatak.presentation.models;

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
	private final WeekSwitcher week;
	private final Runnable onWeekChanged;

	private Department selectedDepartment = null;
	private final ObservableList<Department> departmentList = FXCollections.observableArrayList();
	private final Consumer<Department> selectDepartmentSync;
	
	private Semester selectedSemester = null;
	private final ObservableList<Semester> semesterList = FXCollections.observableArrayList();
	private final Consumer<Semester> selectSemesterSync;
	
	private final ObservableValue<Timetable> timetable = new SimpleObjectProperty<>(null);
	
	private ScheduleItem selectedItem = null;

	public TimetableModel(Runnable onWeekChanged, Consumer<Department> selectDepartmentSync, Consumer<Semester> selectSemesterSync) {
		this.onWeekChanged = onWeekChanged;
		this.selectDepartmentSync = selectDepartmentSync;
		this.selectSemesterSync = selectSemesterSync;

		week = new WeekSwitcher(LocalDate.now());
	}

	public void initialize() {
		var thread = new Thread(() -> {
			var api = Application.getScheduleSource();
			var deps = api.getAvailableDepartments();
			departmentList.setAll(deps);
			
			var user = Application.getUserManager().getUser();
			var firstDep = departmentList.get(0);
			
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

	public void setSelectedDepartment(Department selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		// TODO: fetch semesters and select default or first
		
		if (false) {
			selectDepartmentSync.accept(null);
		}
	}

	public Semester getSelectedSemester() {
		return selectedSemester;
	}

	public void setSelectedSemester(Semester selectedSemester) {
		this.selectedSemester = selectedSemester;
		// TODO: fetch timetable
		
		if (false) {
			selectSemesterSync.accept(null);
		}
	}

	public ObservableList<Department> getDepartmentList() {
		return departmentList;
	}

	public ObservableList<Semester> getSemesterList() {
		return semesterList;
	}
	
	// timetable

	public Timetable getTimetable() {
		return timetable.getValue();
	}

	public ObservableValue<Timetable> timetableProperty() {
		return timetable;
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
		onWeekChanged.run();
	}

	public void previousWeek() {
		week.setToPreviousWeek();
		onWeekChanged.run();
	}

	public void nextWeek() {
		week.setToNextWeek();
		onWeekChanged.run();
	}

	public String getCurrentWeekTimestamp(DateTimeFormatter format) {
		var monday = week.getThisMonday();

		return "%s - %s".formatted(
			monday.format(format),
			monday.plusDays(4).format(format)
		);
	}
}
