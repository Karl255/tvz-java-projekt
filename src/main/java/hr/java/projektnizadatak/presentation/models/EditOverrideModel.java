package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditOverrideModel {
	private ScheduleOverride originalScheduleOverride;
	private ObservableList<OverrideDataModel> replacements;
	private OverrideDataModel selected = null;

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
				manager.getForSubdepartment(),
				manager.getForSemester(),
				Application.getUserManager().getUser().username()
			);
	}
}
