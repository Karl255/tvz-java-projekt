package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
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
		return store.read();
	}

	public ScheduleOverride getOverrideFor(ScheduleItem item) throws DataNoLongerValidException {
		var items = store.read();

		if (item.isOriginal()) {
			return items.stream()
				.filter(o -> o.original().effectivelyEqual(item))
				.findFirst()
				.orElse(null);
		} else {
			return items.stream()
				.filter(o -> o.replacements().stream().anyMatch(i -> i.effectivelyEqual(item)))
				.findFirst()
				.orElseThrow(DataNoLongerValidException::new);
		}
	} 
	
	public void updateOverride(ScheduleOverride oldOverride, ScheduleOverride newOverride) {
		if (oldOverride != null) {
			store.update(oldOverride, newOverride);
		} else {
			if (newOverride != null) {
				store.create(newOverride);
			}
		}
	}
	
	public void deleteOverride(ScheduleOverride override) {
		store.delete(override);
	}
}
