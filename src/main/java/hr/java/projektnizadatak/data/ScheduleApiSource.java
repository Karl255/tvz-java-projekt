package hr.java.projektnizadatak.data;

import com.google.gson.Gson;
import hr.java.projektnizadatak.application.ScheduleSource;
import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.data.api_response.ApiDepartment;
import hr.java.projektnizadatak.data.api_response.ApiEvent;
import hr.java.projektnizadatak.data.api_response.ApiSemester;
import javafx.util.Pair;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ScheduleApiSource implements ScheduleSource {
	private static final URI AVAILABLE_DEPARTMENTS_ENDPOINT = URI.create("https://homer.tvz.hr/CalendarJson/Departments");
	private static final String AVAILABLE_SEMESTERS_ENDPOINT_BASE = "https://homer.tvz.hr/CalendarJson/Semesters";
	private static final String TIMETABLE_ENDPOINT_BASE = "https://homer.tvz.hr/CalendarJson";
	
	private static final DateTimeFormatter API_DATE_FORMAT = DateTimeFormatter.ISO_DATE;
	
	private static final Gson gson = new Gson();
	
	public List<Department> getAvailableDepartments() {
		String json = HttpUtil.fetchFromEndpoint(AVAILABLE_DEPARTMENTS_ENDPOINT);
		
		return Arrays.stream(gson.fromJson(json, ApiDepartment[].class))
			.map(ApiDepartment::toDepartment)
			.toList();
	}
	
	public List<Semester> getAvailableSemesters(String departmentCode, int year) {
		var uri = HttpUtil.buildUriWithParams(AVAILABLE_SEMESTERS_ENDPOINT_BASE,
			new Pair<>("department", departmentCode),
			new Pair<>("year", Integer.toString(year))
		);

		String json = HttpUtil.fetchFromEndpoint(uri);
		
		return Arrays.stream(gson.fromJson(json, ApiSemester[].class))
			.map(ApiSemester::toSemester)
			.toList();
	}

	public Timetable getTimetable(String subdepartment, int semester, int year, LocalDate start, int days) {
		var uri = HttpUtil.buildUriWithParams(TIMETABLE_ENDPOINT_BASE,
			new Pair<>("department", subdepartment),
			new Pair<>("semester", Integer.toString(semester)),
			new Pair<>("year", Integer.toString(year)),
			new Pair<>("start", start.format(API_DATE_FORMAT)),
			new Pair<>("end", start.plusDays(days - 1).format(API_DATE_FORMAT))
		);

		String json = HttpUtil.fetchFromEndpoint(uri);
		var events = gson.fromJson(json, ApiEvent[].class);
		
		var scheduleItems = Arrays.stream(events)
			.filter(e -> !e.allDay())
			.map(ApiEvent::toScheduleItem)
			.toList();
		
		var holidays = Arrays.stream(events)
			.filter(ApiEvent::allDay)
			.map(ApiEvent::toHoliday)
			.toList();
		
		return new Timetable(scheduleItems, holidays);
	}
}
