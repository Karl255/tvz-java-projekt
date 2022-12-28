package hr.java.projektnizadatak.application.entities;

import java.time.LocalDateTime;

// TODO: replace String title with atomic data (info)
public record ScheduleItem(long id, String title, LocalDateTime start, LocalDateTime end) {
}
