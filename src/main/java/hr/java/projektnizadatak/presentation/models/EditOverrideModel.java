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
	private ArrayList<Long> removedReplacementIds = new ArrayList<>();

	public ScheduleOverride getOriginalScheduleOverride() {
		return originalScheduleOverride;
	}

	public ObservableList<OverrideDataModel> getReplacements() {
		return replacements;
	}

	public void initialize() {
		var manager = Application.getOverrideManager();

		originalScheduleOverride = Util.unlessNullGet(
			manager.getOverrideForOriginalId(manager.getItemBeingEdited().originalId()),
			() -> manager.getDefault(manager.getItemBeingEdited())
		);

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
		System.out.printf("Selected id = %d%n", selected.getDbId());
	}

	public void addReplacement(OverrideDataModel replacement) {
		replacements.add(replacement);
	}

	public void save() {
		var manager = Application.getOverrideManager();

		var newReplacements = replacements.stream()
			.map(OverrideDataModel::toOverrideData)
			.toList();

		originalScheduleOverride = Application.getOverrideManager()
			.saveOverride(
				originalScheduleOverride.withReplacements(newReplacements),
				removedReplacementIds,
				manager.getForSubdepartment(),
				manager.getForSemester(),
				Application.getUserManager().getUser().username()
			);
		
		removedReplacementIds.clear();
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
}
