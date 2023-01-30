package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.OverrideData;
import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditOverrideModel {
	private ScheduleOverride originalScheduleOverride;
	private ObservableList<OverrideDataModel> replacements;

	public ScheduleOverride getOriginalScheduleOverride() {
		return originalScheduleOverride;
	}

	public ObservableList<OverrideDataModel> getReplacements() {
		return replacements;
	}

	public void initialize() {
		var manager = Application.getOverrideManager();

		originalScheduleOverride = Util.unlessNullGet(
			manager.getOverrideForOriginalId(manager.getItemBeingEdited().dbId()),
			() -> ScheduleOverride.createDefault(manager.getItemBeingEdited())
		);

		replacements = FXCollections.observableArrayList(
			originalScheduleOverride.replacements().stream()
				.map(OverrideDataModel::new)
				.toList()
		);
	}
}
