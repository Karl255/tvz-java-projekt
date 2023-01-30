package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScheduleOverridesManager {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleOverridesManager.class);

	private final OverridesStore store;

	private ScheduleItem itemBeingEdited;

	public ScheduleOverridesManager(OverridesStore store) {
		this.store = store;
	}
	
	public void setItemBeingEdited(ScheduleItem item) {
		itemBeingEdited = item;
	}

	public ScheduleItem getItemBeingEdited() {
		return itemBeingEdited;
	}

	public ScheduleOverride getDefault(ScheduleItem item) {
		return new ScheduleOverride(item, List.of(OverrideData.fromOriginal(item)));
	}

	public List<ScheduleOverride> getAllOverrides(String subdepartment, int semester) {
		return store.readAllOverridesFor(subdepartment, semester);
	}

	public ScheduleOverride getOverrideForOriginalId(Long id) {
		if (id != null) {
			return store.readOverride(id);
		} else {
			return null;
		}
	}
	
	private void logChange(ScheduleOverride oldValue, ScheduleOverride newValue) {
		var user = Application.getUserManager().getUser();
		var change = Change.create(user, oldValue, newValue);
		ChangesManager.getInstance().addChange(change);
	}
}
