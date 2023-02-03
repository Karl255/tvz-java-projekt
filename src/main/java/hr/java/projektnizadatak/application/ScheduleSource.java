package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Department;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.Timetable;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.NetworkErrorException;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleSource {
	List<Department> getAvailableDepartments() throws NetworkErrorException;
	List<Semester> getAvailableSemesters(String departmentCode, int year) throws NetworkErrorException;
	Timetable getTimetable(String subdepartment, int semester, int year, LocalDate start, int days) throws NetworkErrorException;
}
