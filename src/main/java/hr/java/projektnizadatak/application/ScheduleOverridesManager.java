package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleItem;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;
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

	public List<ScheduleOverride> getAllUserOverrides(String username) throws DataStoreException {
		return store.readAllUserOverrides(username);
	}

	public List<ScheduleOverride> getAllUserOverridesFor(String username, String subdepartment, int semester) throws DataStoreException {
		return store.readAllUserOverridesFor(username, subdepartment, semester);
	}

	public ScheduleOverride getOverrideForOriginalId(Long id) throws DataStoreException {
		if (id != null) {
			return store.readOverride(id);
		} else {
			return null;
		}
	}

	// update

	// delete

	public void deleteOverride(ScheduleOverride scheduleOverride) throws DataStoreException {
		store.deleteReplacements(
			scheduleOverride
				.replacements()
				.stream()
				.map(OverrideData::id).toList()
		);

		store.deleteOriginal(scheduleOverride.original().originalId());
		
		logChange(scheduleOverride, null);
	}

	// mixed

	public long saveOverride(ScheduleOverride scheduleOverride, List<Long> removedReplacementIds, String forSubdepartment, Integer forSemester, String forUsername) throws DataStoreException {
		if (scheduleOverride.original().originalId() != null) {
			// edited override

			var previousValue = getOverrideForOriginalId(scheduleOverride.original().originalId());
			
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

			logChange(previousValue, scheduleOverride);
			
			return scheduleOverride.original().originalId();
		} else {
			// new override

			if (forSubdepartment == null || forSemester == null) {
				String m = "All identifying parameters are null";
				logger.error(m);
				
				throw new UnreachableCodeException(m);
			}

			long originalId = store.createOriginal(scheduleOverride.original(), forSubdepartment, forSemester, forUsername);
			store.createReplacements(scheduleOverride.replacements(), originalId);

			logChange(null, scheduleOverride);
			
			return originalId;
		}
	}

	private void logChange(ScheduleOverride oldValue, ScheduleOverride newValue) {
		var user = Application.getUserManager().getLoggedInUser();
		var change = Change.create(user, oldValue, newValue);
		ChangesManager.getInstance().addChange(change);
	}
}
