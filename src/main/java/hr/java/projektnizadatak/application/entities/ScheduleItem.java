package hr.java.projektnizadatak.application.entities;

import hr.java.projektnizadatak.shared.Util;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record ScheduleItem(
	Long dbId,
	String courseName,
	String className,
	String professor,
	ClassType classType,
	String classroom,
	String note,
	String group,
	DayOfWeek weekday,
	LocalTime start,
	LocalTime end,
	boolean isOriginal
) implements Serializable {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");
	private static final DateTimeFormatter TIMESTAMP_FULL_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	public String getTimestampShort() {
		return "%s - %s".formatted(
			start.format(TIMESTAMP_FORMAT),
			end.format(TIMESTAMP_FORMAT)
		);
	}

	public String getTimestampFull() {
		return "%s - %s".formatted(
			start.format(TIMESTAMP_FULL_FORMAT),
			end.format(TIMESTAMP_FULL_FORMAT)
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}

		ScheduleItem that = (ScheduleItem) o;

		return classType == that.classType
			&& isOriginal == that.isOriginal
			&& Objects.equals(courseName, that.courseName)
			&& Objects.equals(className, that.className)
			&& Objects.equals(professor, that.professor)
			&& Objects.equals(classroom, that.classroom)
			&& Objects.equals(note, that.note)
			&& Objects.equals(group, that.group)
			&& Objects.equals(weekday, that.weekday)
			&& Objects.equals(start, that.start)
			&& Objects.equals(end, that.end);
	}

	public boolean matches(ScheduleItem other) {
		return start.equals(other.start)
			&& end.equals(other.end)
			&& weekday == other.weekday
			&& classType == other.classType
			&& className.equals(other.className)
			&& classroom.equals(other.classroom);
	}
}

