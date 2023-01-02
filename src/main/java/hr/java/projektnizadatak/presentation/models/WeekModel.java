package hr.java.projektnizadatak.presentation.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WeekModel {
	private LocalDate thisMonday;

	public WeekModel() {
		setCurrentWeek();
	}
	
	public WeekModel(LocalDate date) {
		this.thisMonday = date.minusDays(date.getDayOfWeek().getValue() - 1);
	}
	
	public LocalDate setCurrentWeek() {
		var today = LocalDate.now();
		return thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1);
	}
	
	public LocalDate setPreviousWeek() {
		return thisMonday = thisMonday.minusDays(7);
	}
	
	public LocalDate setNextWeek() {
		return thisMonday = thisMonday.plusDays(7);
	}
	
	public LocalDate getThisMonday() {
		return thisMonday;
	}
	
	public String toTimespan(int days, DateTimeFormatter format) {
		return thisMonday.format(format)
			+ " - "
			+ thisMonday.plusDays(days - 1).format(format);
	}
}
