package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.util.List;

public interface OverridesStore {
	void createSigle(ScheduleOverride scheduleOverride);

	ScheduleOverride readOverride(long id);
	
	List<ScheduleOverride> readAllOverridesFor(String subdepartment, int semester);

	void updateSingle(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride);

	void deleteSingle(ScheduleOverride scheduleOverride);
}
