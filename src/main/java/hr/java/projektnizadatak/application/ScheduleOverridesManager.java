package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleOverridesManager {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleOverridesManager.class);

	private final OverridesStore store;

	private ScheduleItem itemBeingEdited;
	private String forSubdepartment;
	private Integer forSemester;

	public ScheduleOverridesManager(OverridesStore store) {
		this.store = store;
	}

	public ScheduleItem getItemBeingEdited() {
		return itemBeingEdited;
	}

	public String getForSubdepartment() {
		return forSubdepartment;
	}

	public Integer getForSemester() {
		return forSemester;
	}

	public void setItemBeingEdited(ScheduleItem item, String onSubdepartment, Integer onSemester) {
		itemBeingEdited = item;
		this.forSubdepartment = onSubdepartment;
		this.forSemester = onSemester;
	}

	public ScheduleOverride getDefault(ScheduleItem item) {
		return new ScheduleOverride(
			item,
			List.of(OverrideData.fromOriginal(item))
		);
	}

	// create

	// read

	public List<ScheduleOverride> getAllUserOverrides(String username) {
		return store.readAllUserOverrides(username);
	}

	public List<ScheduleOverride> getAllUserOverridesFor(String username, String subdepartment, int semester) {
		return store.readAllUserOverridesFor(username, subdepartment, semester);
	}

	public ScheduleOverride getOverrideForOriginalId(Long id) {
		if (id != null) {
			return store.readOverride(id);
		} else {
			return null;
		}
	}

	// update

	// delete

	public void deleteOverride(ScheduleOverride scheduleOverride) {
		store.deleteReplacements(
			scheduleOverride
				.replacements()
				.stream()
				.map(OverrideData::id).toList()
		);

		store.deleteOriginal(scheduleOverride.original().originalId());
	}

	// mixed

	public long saveOverride(ScheduleOverride scheduleOverride, List<Long> removedReplacementIds, String forSubdepartment, Integer forSemester, String forUsername) {
		if (scheduleOverride.original().originalId() != null) {
			// edited override

			var exists = scheduleOverride.replacements().stream()
				.collect(Collectors.groupingBy(replacement -> replacement.id() != null));

			store.updateOriginal(scheduleOverride.original());

			if (exists.containsKey(true)) {
				store.updateReplacements(exists.get(true));
			}

			if (exists.containsKey(false)) {
				store.createReplacements(exists.get(false), scheduleOverride.original().originalId());
			}

			store.deleteReplacements(removedReplacementIds);

			return scheduleOverride.original().originalId();
		} else {
			// new override

			if (forSubdepartment == null || forSemester == null) {
				// TODO
				throw new RuntimeException();
			}

			long originalId = store.createOriginal(scheduleOverride.original(), forSubdepartment, forSemester, forUsername);
			store.createReplacements(scheduleOverride.replacements(), originalId);

			return originalId;
		}
	}

	private void logChange(ScheduleOverride oldValue, ScheduleOverride newValue) {
		// TODO
		var user = Application.getUserManager().getUser();
		var change = Change.create(user, oldValue, newValue);
		ChangesManager.getInstance().addChange(change);
	}
}
