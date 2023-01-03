package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.util.List;

public interface OverridesStore {
	void create(ScheduleOverride scheduleOverride);

	List<ScheduleOverride> read();

	void update(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride);

	void delete(ScheduleOverride scheduleOverride);
}
