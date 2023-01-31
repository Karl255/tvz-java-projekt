package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.presentation.util.WeekSwitcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimetableModel {
	private final WeekSwitcher week;

	public TimetableModel() {
		week = new WeekSwitcher(LocalDate.now());
	}
	
	// week-related stuff
	
	public void switchToCurrentWeek() {
		week.setWeek(LocalDate.now());
	}

	public void previousWeek() {
		week.setToPreviousWeek();
	}
	
	public void nextWeek() {
		week.setToNextWeek();
	}

	public String getCurrentWeekTimestamp(DateTimeFormatter format) {
		var monday = week.getThisMonday();
		
		return "%s - %s".formatted(
			monday.format(format),
			monday.plusDays(4).format(format)
		);
	}
}
