package hr.java.projektnizadatak.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleOverridesManager {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleOverridesManager.class);
	
	private final OverridesStore store;

	public ScheduleOverridesManager(OverridesStore store) {
		this.store = store;
	}
}
