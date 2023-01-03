package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.OverridesStore;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;

import java.nio.file.Path;

public class ScheduleOverridesFileStore extends ObjectStore<ScheduleOverride> implements OverridesStore {
	private static final Path OVERRIDES_FILE_PATH = Path.of("data/overrides.dat");

	public ScheduleOverridesFileStore() {
		super(OVERRIDES_FILE_PATH, ScheduleOverride.class);
	}
}
