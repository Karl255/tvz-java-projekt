package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AllOverridesModel {
	private final ObservableList<ScheduleOverride> overridesList = FXCollections.observableArrayList();
	private ScheduleOverride selected;
	
	public void initialize() {
		var username = Application.getUserManager().getUser().username();
		var overrides = Application.getOverrideManager().getAllUserOverrides(username);
		overridesList.setAll(overrides);
	}

	public ObservableList<ScheduleOverride> getOverridesList() {
		return overridesList;
	}

	public ScheduleOverride getSelected() {
		return selected;
	}

	public void setSelected(ScheduleOverride selected) {
		this.selected = selected;
	}
}
