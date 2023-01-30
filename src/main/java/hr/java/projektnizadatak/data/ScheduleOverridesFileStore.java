package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.OverridesStore;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.nio.file.Path;
import java.util.List;

public class ScheduleOverridesFileStore extends ObjectStore<ScheduleOverride> implements OverridesStore {
	private static final Path OVERRIDES_FILE_PATH = Path.of("data/overrides.dat");

	public ScheduleOverridesFileStore() {
		super(OVERRIDES_FILE_PATH, ScheduleOverride.class);
	}

	@Override
	public void createSigle(ScheduleOverride scheduleOverride) {
		super.create(scheduleOverride);
	}
	
	@Override
	public List<ScheduleOverride> readAll() {
		return super.read();
	}
	
	@Override
	public void updateSingle(ScheduleOverride oldScheduleOverride, ScheduleOverride newScheduleOverride) {
		super.update(oldScheduleOverride, newScheduleOverride);
	}
	
	@Override
	public void deleteSingle(ScheduleOverride scheduleOverride) {
		super.delete(scheduleOverride);
	}
}
