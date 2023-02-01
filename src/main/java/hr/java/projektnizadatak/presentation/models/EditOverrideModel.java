package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EditOverrideModel {
	private ScheduleOverride originalScheduleOverride;
	private ObservableList<OverrideDataItemModel> replacements;
	private OverrideDataItemModel selected = null;
	private final ArrayList<Long> removedReplacementIds = new ArrayList<>();

	public void initialize() {
		loadFromStore(Application.getOverrideManager().getItemBeingEdited().originalId());
	}

	public ScheduleOverride getOriginalScheduleOverride() {
		return originalScheduleOverride;
	}

	public ObservableList<OverrideDataItemModel> getReplacements() {
		return replacements;
	}

	private void loadFromStore(Long id) {
		var manager = Application.getOverrideManager();

		originalScheduleOverride = id != null
			? manager.getOverrideForOriginalId(id)
			: manager.getDefault(manager.getItemBeingEdited());

		replacements = FXCollections.observableArrayList(
			originalScheduleOverride.replacements().stream()
				.map(OverrideDataItemModel::new)
				.toList()
		);
	}

	public OverrideDataItemModel getSelected() {
		return selected;
	}

	public void setSelected(OverrideDataItemModel overrideDataItemModel) {
		selected = overrideDataItemModel;
	}

	public void addReplacement(OverrideDataItemModel replacement) {
		replacements.add(replacement);
	}

	public void save() {
		var manager = Application.getOverrideManager();

		var newReplacements = replacements.stream()
			.map(OverrideDataItemModel::toOverrideData)
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

	public void deleteAll() {
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
