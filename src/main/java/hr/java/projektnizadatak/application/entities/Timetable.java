package hr.java.projektnizadatak.application.entities;

import java.util.List;

public record Timetable(
	List<ScheduleItem> scheduleItems,
	List<Holiday> holidays,
	String forSubdepartment,
	int forSemester
) {
}
