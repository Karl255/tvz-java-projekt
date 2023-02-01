package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record OverrideData(
	Long id,
	String professor,
	ClassType classType,
	String classroom,
	String note,
	String group,
	LocalTime start,
	LocalTime end
) implements Serializable {
	private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("H:mm");
	private static final DateTimeFormatter TIMESTAMP_FULL_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	public static OverrideData fromOriginal(ScheduleItem item) {
		return new OverrideData(
			null,
			item.professor(),
			item.classType(),
			item.classroom(),
			item.note(),
			item.group(),
			item.start(),
			item.end()
		);
	}
	
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
}
