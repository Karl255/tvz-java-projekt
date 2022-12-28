package hr.java.projektnizadatak.data.api_response;

import java.time.LocalDateTime;

public record ApiEvent(String id, String title, String start, String end, boolean allDay, String color, boolean editable) {
}
