package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleOverridesManager {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleOverridesManager.class);
	
	private final OverridesStore store;

	private ScheduleItem itemBeingEdited;
	
	public ScheduleOverridesManager(OverridesStore store) {
		this.store = store;
	}

	public ScheduleItem getItemBeingEdited() {
		return itemBeingEdited;
	}

	public void setItemBeingEdited(ScheduleItem itemBeingEdited) {
		this.itemBeingEdited = itemBeingEdited;
	}
}
