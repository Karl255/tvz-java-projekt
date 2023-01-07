package hr.java.projektnizadatak.data.api_response;

import hr.java.projektnizadatak.application.entities.ClassType;
import hr.java.projektnizadatak.application.entities.Holiday;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public record ApiEvent(String id, String title, String start, String end, boolean allDay, String color, boolean editable) {
	private static final Logger logger = LoggerFactory.getLogger(ApiEvent.class);
	
	private static final DateTimeFormatter SCHEDULE_ITEM_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;
	private static final DateTimeFormatter EVENT_DATE_FORMAT = DateTimeFormatter.ISO_DATE;

	private static final Pattern TITLE_PARSING_REGEX = Pattern.compile(
		"^<strong>([^<]+)</strong> - ([^<]+)<br/>" +
		"([^<]+)<br/>" +
		"Učionica: ([^<]+)<br/>" +
		"Smjer: ([^<]+)<br/>" +
		"(?:Napomena: ([^<]+)<br/>)?" +
		"(?:Grupa: ([^<]+)<br/>)?" +
		"Broj studenata na kolegiju: (.+)$"
	);
	
	public ScheduleItem toScheduleItem() {
		long numeric_id = Long.parseLong(id);
		var startTime = LocalDateTime.parse(start, SCHEDULE_ITEM_DATE_TIME_FORMAT);
		var endTime = LocalDateTime.parse(end, SCHEDULE_ITEM_DATE_TIME_FORMAT);
		
		var results = TITLE_PARSING_REGEX
			.matcher(title)
			.results()
			.findFirst();

		if (results.isEmpty()) {
			logger.error("Failed to parse title field from API: " + title);
			
			return new ScheduleItem(
				numeric_id,
				"ERROR",
				"ERROR",
				"ERROR",
				ClassType.OTHER,
				"ERROR",
				"ERROR",
				"ERROR",
				startTime.toLocalDate(),
				startTime.toLocalTime(),
				endTime.toLocalTime(),
				true
			);
		}
		
		var matches = results.get();
		
		return new ScheduleItem(
			numeric_id,
			matches.group(5).trim(),
			matches.group(1).trim(),
			matches.group(3).trim(),
			ClassType.parse(matches.group(2).trim()),
			matches.group(4).trim(),
			trimOrNull(matches.group(6)),
			trimOrNull(matches.group(7)),
			startTime.toLocalDate(),
			startTime.toLocalTime(),
			endTime.toLocalTime(),
			true
		);
	}
	
	private static String trimOrNull(String string) {
		return string != null ? string.trim() : null;
	}
	
	public Holiday toHoliday() {
		return new Holiday(title, LocalDate.parse(start, EVENT_DATE_FORMAT));
	}
}
