package hr.java.projektnizadatak.data.api_response;

import hr.java.projektnizadatak.application.entities.Holiday;
import hr.java.projektnizadatak.application.entities.ScheduleItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ApiEvent(String id, String title, String start, String end, boolean allDay, String color, boolean editable) {
	private static final DateTimeFormatter SCHEDULE_ITEM_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;
	private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ISO_DATE;
	
	public ScheduleItem toScheduleItem() {
		long item_id = Long.parseLong(id);
		var startTime = LocalDateTime.parse(start, SCHEDULE_ITEM_DATE_TIME_FORMAT);
		var endTime = LocalDateTime.parse(end, SCHEDULE_ITEM_DATE_TIME_FORMAT);
		
		return new ScheduleItem(item_id, title, startTime, endTime);
	}
	
	public Holiday toHoliday() {
		return new Holiday(title, LocalDate.parse(start, EVENT_DATE_FORMAT));
	}
}
