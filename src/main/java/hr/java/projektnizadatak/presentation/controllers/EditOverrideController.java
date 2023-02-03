package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.OverrideDataModel;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EditOverrideController {
	private ScheduleOverride originalScheduleOverride;
	private final ObservableList<OverrideDataModel> replacements;
	private OverrideDataModel selected = null;
	private final ArrayList<Long> removedReplacementIds = new ArrayList<>();

	public EditOverrideController() {
		replacements = FXCollections.observableArrayList();
	}

	public void initialize() throws DataStoreException {
		loadFromStore(Application.getOverrideManager().getItemBeingEdited().originalId());
	}

	public ScheduleOverride getOriginalScheduleOverride() {
		return originalScheduleOverride;
	}

	public ObservableList<OverrideDataModel> getReplacements() {
		return replacements;
	}

	private void loadFromStore(Long id) throws DataStoreException {
		var manager = Application.getOverrideManager();

		originalScheduleOverride = id != null
			? manager.getOverrideForOriginalId(id)
			: manager.getDefault(manager.getItemBeingEdited());

		replacements.setAll(
			originalScheduleOverride.replacements().stream()
				.map(OverrideDataModel::new)
				.toList()
		);
	}

	public OverrideDataModel getSelected() {
		return selected;
	}

	public void setSelected(OverrideDataModel overrideDataModel) {
		selected = overrideDataModel;
	}

	public void addReplacement(OverrideDataModel replacement) {
		replacements.add(replacement);
	}

	public void save() throws DataStoreException {
		var manager = Application.getOverrideManager();

		var newReplacements = replacements.stream()
			.map(OverrideDataModel::toOverrideData)
			.toList();

		var id = manager.saveOverride(
			originalScheduleOverride.withReplacements(newReplacements),
			removedReplacementIds,
			manager.getForSubdepartment(),
			manager.getForSemester(),
			Application.getUserManager().getLoggedInUser().username()
		);

		removedReplacementIds.clear();

		loadFromStore(id);
	}

	public void deleteSelected() {
		if (selected != null) {
			var toRemove = selected;
			if (selected.getDbId() != null) {
				removedReplacementIds.add(toRemove.getDbId());
			}
			replacements.remove(toRemove);
			selected = null;
		}
	}

	public void deleteAll() throws DataStoreException {
		Application.getOverrideManager().deleteOverride(originalScheduleOverride);
	}

	public boolean needsToSave() {
		return !existsInStore()
			|| removedReplacementIds.size() > 0
			|| replacementsChanged();
	}

	public boolean existsInStore() {
		return originalScheduleOverride.original().originalId() != null;
	}

	public boolean replacementsChanged() {
		return replacements.stream().anyMatch(r -> r.getDbId() == null) // not in store
			|| replacements.stream().anyMatch(r -> r.getDbId() != null // in store and not in stored data
				&& originalScheduleOverride.replacements().stream().noneMatch(r::equals)
		);
	}
}
