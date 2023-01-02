package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.Timetable;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleSource {
	List<Department> getAvailableDepartments();
	List<Semester> getAvailableSemesters(String departmentCode, int year);
	Timetable getTimetable(String subdepartment, int semester, int year, LocalDate start, int days);
}
