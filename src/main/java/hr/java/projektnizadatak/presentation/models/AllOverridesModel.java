package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.ScheduleOverride;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.FXUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;

public class AllOverridesModel {
	private List<ScheduleOverride> allOverrides;
	private final ObservableList<ScheduleOverride> overridesList = FXCollections.observableArrayList();
	private ScheduleOverride selected;

	public void initialize() {
		var username = Application.getUserManager().getLoggedInUser().username();
		allOverrides = Application.getOverrideManager().getAllUserOverrides(username);

		overridesList.setAll(allOverrides);
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

	public void setFilter(String filterText) {
		var filtered = filterText.equals("")
			? allOverrides
			: allOverrides.stream()
			.filter(o -> filterSingle(o, filterText))
			.toList();

		overridesList.setAll(filtered);
	}

	private boolean filterSingle(ScheduleOverride scheduleOverride, String filter) {
		filter = filter.toLowerCase();
		var original = scheduleOverride.original();

		return original.courseName().toLowerCase().contains(filter)
			|| original.className().toLowerCase().contains(filter)
			|| original.classType().getName().toLowerCase().contains(filter)
			|| FXUtil.weekdayName(original.weekday()).toLowerCase().contains(filter)
			|| original.getTimestampFull().toLowerCase().contains(filter)
			|| (original.group() != null && original.group().toLowerCase().contains(filter))
			|| (original.note() != null && original.note().toLowerCase().contains(filter));
	}

	public void deleteSelected() {
		if (selected == null) {
			return;
		}
		
		Application.getOverrideManager().deleteOverride(selected);

		allOverrides = allOverrides.stream()
			.filter(o -> !Objects.equals(o.original().originalId(), selected.original().originalId()))
			.toList();

		overridesList.remove(selected);
		
		selected = null;
	}
}
