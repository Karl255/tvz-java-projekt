package hr.java.projektnizadatak.data;

import com.google.gson.Gson;
import hr.java.projektnizadatak.application.ScheduleSource;
import hr.java.projektnizadatak.data.api_response.ApiDepartment;
import hr.java.projektnizadatak.data.api_response.ApiEvent;
import hr.java.projektnizadatak.data.api_response.ApiSemester;

import java.net.URI;
import java.util.List;

public class ScheduleApiSource implements ScheduleSource {
	private static final URI AVAILABLE_DEPARTMENTS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Departments");
	private static final URI AVAILABLE_SEMESTERS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Semesters?department=rac&year=2022");
	private static final URI CALENDAR_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson?department=PRIN&semester=3&year=2022&start=2022-10-03&end=2022-10-09");
	
	private static final Gson gson = new Gson();
	
	public List<ApiDepartment> fetchAvailableDepartments() {
		String json = HttpUtil.fetchFromEndpoint(AVAILABLE_DEPARTMENTS_ENDPOINT);
		
		return List.of(gson.fromJson(json, ApiDepartment[].class));
	}
	
	public List<ApiSemester> fetchAvailableSemesters() {
		String json = HttpUtil.fetchFromEndpoint(AVAILABLE_SEMESTERS_ENDPOINT);
		return List.of(gson.fromJson(json, ApiSemester[].class));
	}
	
	public List<ApiEvent> fetchCalendar() {
		String json = HttpUtil.fetchFromEndpoint(CALENDAR_ENDPOINT);
		
		return List.of(gson.fromJson(json, ApiEvent[].class));
	}
}
