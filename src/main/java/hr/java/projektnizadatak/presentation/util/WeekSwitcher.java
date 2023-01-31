package hr.java.projektnizadatak.presentation.util;

import java.time.LocalDate;

public class WeekSwitcher {
	private LocalDate thisMonday;

	public WeekSwitcher(LocalDate date) {
		setWeek(date);
	}
	
	public LocalDate setWeek(LocalDate date) {
		return thisMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);
	}
	
	public LocalDate setToPreviousWeek() {
		return thisMonday = thisMonday.minusDays(7);
	}
	
	public LocalDate setToNextWeek() {
		return thisMonday = thisMonday.plusDays(7);
	}
	
	public LocalDate getThisMonday() {
		return thisMonday;
	}
}
