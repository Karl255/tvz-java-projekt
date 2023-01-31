package hr.java.projektnizadatak.application.entities;

import java.time.LocalDate;
import java.util.List;

public record Timetable(
	List<ScheduleItem> scheduleItems,
	List<Holiday> holidays,
	LocalDate forWeekMonday,
	String forSubdepartment,
	int forSemester
) {
	public Timetable withScheduleItems(List<ScheduleItem> items) {
		return new Timetable(
			items,
			holidays,
			forWeekMonday,
			forSubdepartment,
			forSemester
		);
	}
}
