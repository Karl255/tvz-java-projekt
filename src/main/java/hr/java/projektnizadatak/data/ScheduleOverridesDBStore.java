package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.OverridesStore;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.util.List;

public class ScheduleOverridesDBStore implements OverridesStore {
	@Override
	public void createSigle(ScheduleOverride scheduleOverride) {
		
	}

	@Override
	public List<ScheduleOverride> readAll() {
		return null;
	}

	@Override
	public void updateSingle(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride) {

	}

	@Override
	public void deleteSingle(ScheduleOverride scheduleOverride) {

	}
}
