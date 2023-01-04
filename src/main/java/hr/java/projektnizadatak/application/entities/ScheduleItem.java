package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
) implements Serializable {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");

	public String getTimestamp() {
		return start().format(TIMESTAMP_FORMAT) + " - " + end().format(TIMESTAMP_FORMAT);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}

		ScheduleItem that = (ScheduleItem) o;

		return id == that.id
			&& classType == that.classType
			&& isOriginal == that.isOriginal
			&& Objects.equals(courseName, that.courseName)
			&& Objects.equals(className, that.className)
			&& Objects.equals(professor, that.professor)
			&& Objects.equals(classroom, that.classroom)
			&& Objects.equals(note, that.note)
			&& Objects.equals(group, that.group)
			&& Objects.equals(date, that.date)
			&& Objects.equals(start, that.start)
			&& Objects.equals(end, that.end);
	}

	public boolean effectivelyEqual(ScheduleItem other) {
		return start.equals(other.start)
			&& end.equals(other.end)
			&& classType == other.classType
			&& classroom.equals(other.classroom);
	}
}
