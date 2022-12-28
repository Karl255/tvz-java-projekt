package hr.java.projektnizadatak.application.entities;

import java.util.List;

public record Calendar(List<ScheduleItem> scheduleItems, List<Holiday> holidays) {
}
