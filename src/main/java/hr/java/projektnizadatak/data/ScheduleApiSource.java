package hr.java.projektnizadatak.data;

import com.google.gson.Gson;
import hr.java.projektnizadatak.application.ScheduleSource;
import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.data.api_response.ApiDepartment;
import hr.java.projektnizadatak.data.api_response.ApiEvent;
import hr.java.projektnizadatak.data.api_response.ApiSemester;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class ScheduleApiSource implements ScheduleSource {
	private static final URI AVAILABLE_DEPARTMENTS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Departments");
	private static final URI AVAILABLE_SEMESTERS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Semesters?department=rac&year=2022");
	private static final URI CALENDAR_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson?department=PRIN&semester=3&year=2022&start=2022-10-03&end=2022-10-09");
	
	private static final Gson gson = new Gson();
	
	public List<Department> fetchAvailableDepartments() {
		String json = HttpUtil.fetchFromEndpoint(AVAILABLE_DEPARTMENTS_ENDPOINT);
		
		return Arrays.stream(gson.fromJson(json, ApiDepartment[].class))
			.map(ApiDepartment::toDepartment)
			.toList();
	}
	
	public List<Semester> fetchAvailableSemesters() {
		String json = HttpUtil.fetchFromEndpoint(AVAILABLE_SEMESTERS_ENDPOINT);
		return Arrays.stream(gson.fromJson(json, ApiSemester[].class))
			.map(ApiSemester::toSemester)
			.toList();
	}
	
	public Calendar fetchCalendar() {
		String json = HttpUtil.fetchFromEndpoint(CALENDAR_ENDPOINT);
		var events = gson.fromJson(json, ApiEvent[].class);
		
		var scheduleItems = Arrays.stream(events)
			.filter(e -> !e.allDay())
			.map(ApiEvent::toScheduleItem)
			.toList();
		
		var holidays = Arrays.stream(events)
			.filter(ApiEvent::allDay)
			.map(ApiEvent::toHoliday)
			.toList();
		
		return new Calendar(scheduleItems, holidays);
	}
}
