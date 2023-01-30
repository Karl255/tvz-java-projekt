package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.exceptions.DataNoLongerValidException;
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

	public ScheduleItem getItemBeingEdited() {
		return itemBeingEdited;
	}

	public void setItemBeingEdited(ScheduleItem itemBeingEdited) {
		this.itemBeingEdited = itemBeingEdited;
	}
	
	public ScheduleOverride getDefault(ScheduleItem item) {
		return new ScheduleOverride(item, List.of(item));
	}

	public List<ScheduleOverride> getAllOverrides() {
		return store.readAll();
	}

	public ScheduleOverride getOverrideFor(ScheduleItem item) throws DataNoLongerValidException {
		var items = store.readAll();

		if (item.isOriginal()) {
			return items.stream()
				.filter(o -> o.original().effectivelyEqual(item))
				.findFirst()
				.orElse(null);
		} else {
			return items.stream()
				.filter(o -> o.replacements().stream().anyMatch(i -> i.effectivelyEqual(item)))
				.findFirst()
				.orElseThrow(() -> {
					String m = "Previously fetched data from DB cannot be found anymore";
					logger.error(m);
					
					return new DataNoLongerValidException(m);
				});
		}
	} 
	
	public void updateOverride(ScheduleOverride oldOverride, ScheduleOverride newOverride) {
		if (oldOverride != null) {
			if (newOverride != null) {
				store.updateSingle(oldOverride, newOverride);
			} else {
				store.deleteSingle(oldOverride);
			}
		} else {
			if (newOverride != null) {
				store.createSigle(newOverride);
			} else {
				logger.warn("Tried to update null override to null!");
			}
		}
		
		if (oldOverride != null || newOverride != null) {
			logChange(oldOverride, newOverride);
		}
	}
	
	public void deleteOverride(ScheduleOverride override) {
		store.deleteSingle(override);
		logChange(override, null);
	}
	
	private void logChange(ScheduleOverride oldValue, ScheduleOverride newValue) {
		var user = Application.getUserManager().getUser();
		var change = Change.create(user, oldValue, newValue);
		ChangesManager.getInstance().addChange(change);
	}
}
