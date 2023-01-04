package hr.java.projektnizadatak.application.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record ScheduleItem(
	long id,
	String courseName,
	String className,
	String professor,
	ClassType classType,
	String classroom,
	String note,
	String group,
	LocalDate date,
	LocalTime start,
	LocalTime end,
	boolean isOriginal
) {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");

	public String getTimestamp() {
		return start().format(TIMESTAMP_FORMAT) + " - " + end().format(TIMESTAMP_FORMAT);
	}
}
