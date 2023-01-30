package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.util.List;

public interface OverridesStore {
	void createSigle(ScheduleOverride scheduleOverride);

	List<ScheduleOverride> readAll();

	void updateSingle(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride);

	void deleteSingle(ScheduleOverride scheduleOverride);
}
