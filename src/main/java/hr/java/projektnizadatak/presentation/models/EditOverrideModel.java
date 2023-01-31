package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class EditOverrideModel {
	private ScheduleOverride originalScheduleOverride;
	private ObservableList<OverrideDataModel> replacements;
	private OverrideDataModel selected = null;
	private final ArrayList<Long> removedReplacementIds = new ArrayList<>();

	public ScheduleOverride getOriginalScheduleOverride() {
		return originalScheduleOverride;
	}

	public ObservableList<OverrideDataModel> getReplacements() {
		return replacements;
	}

	public void initialize() {
		loadFromStore(Application.getOverrideManager().getItemBeingEdited().originalId());
	}
	
	private void loadFromStore(Long id) {
		var manager = Application.getOverrideManager();
		
		originalScheduleOverride = id != null
			? manager.getOverrideForOriginalId(id)
			: manager.getDefault(manager.getItemBeingEdited());

		replacements = FXCollections.observableArrayList(
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

	public void save() {
		var manager = Application.getOverrideManager();

		var newReplacements = replacements.stream()
			.map(OverrideDataModel::toOverrideData)
			.toList();

		var id = manager.saveOverride(
			originalScheduleOverride.withReplacements(newReplacements),
			removedReplacementIds,
			manager.getForSubdepartment(),
			manager.getForSemester(),
			Application.getUserManager().getUser().username()
		);
		
		removedReplacementIds.clear();
		
		loadFromStore(id);
	}

	public void deleteSelected() {
		if (selected != null) {
			var toRemove = selected;
			removedReplacementIds.add(toRemove.getDbId());
			replacements.remove(toRemove);
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
