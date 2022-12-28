package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ScheduleSource;

import java.net.URI;

public class ScheduleApiSource implements ScheduleSource {
	private static final URI AVAILABLE_DEPARTMENTS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Departments");
	private static final URI AVAILABLE_SEMESTERS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Semesters?department=rac&year=2022");
	private static final URI CALENDAR_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson?department=PRIN&semester=3&year=2022&start=2022-10-03&end=2022-10-09");
	
	public String fetchAvailableDepartments() {
		return HttpUtil.fetchFromEndpoint(AVAILABLE_DEPARTMENTS_ENDPOINT);
	}
	
	public String fetchAvailableSemesters() {
		return HttpUtil.fetchFromEndpoint(AVAILABLE_SEMESTERS_ENDPOINT);
	}
	
	public String fetchCalendar() {
		return HttpUtil.fetchFromEndpoint(CALENDAR_ENDPOINT);
	}
}
