package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public record ScheduleOverride(ScheduleItem original, List<ScheduleItem> replacements) implements Serializable, Recordable {
	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}

		ScheduleOverride that = (ScheduleOverride) o;

		if (!original.equals(that.original)) {return false;}
		return replacements.equals(that.replacements);
	}

	public ScheduleOverride withOriginal(ScheduleItem newOriginal) {
		return new ScheduleOverride(newOriginal, replacements);
	}

	public ScheduleOverride withReplacements(List<ScheduleItem> newReplacements) {
		return new ScheduleOverride(original, new ArrayList<>(newReplacements));
	}

	public ScheduleOverride updateReplacement(ScheduleItem oldReplacement, ScheduleItem newReplacement) {
		var newReplacements = replacements.stream()
			.map(r -> r.equals(oldReplacement)
				? newReplacement
				: oldReplacement)
			.toList();

		return new ScheduleOverride(original, newReplacements);
	}

	public boolean areReplacementsEqual(List<ScheduleItem> other) {
		return replacements.equals(other);
	}

	public static Timetable applyOverrides(Timetable timetable, List<ScheduleOverride> overrides) {
		var mappedItems = timetable.scheduleItems()
			.stream()
			.flatMap(original -> {
				var foundOverride = overrides.stream()
					.filter(ov -> ov.original().effectivelyEqual(original))
					.findFirst();

				if (foundOverride.isPresent()) {
					return foundOverride.get()
						.replacements()
						.stream()
						.map(original::withOverrides);
				} else {
					return Stream.of(original);
				}
			})
			.toList();

		return new Timetable(mappedItems, timetable.holidays());
	}

	@Override
	public String displayShort() {
		// TODO
		return original.courseName() + ": " + original.className() + " -> [" + replacements.size() + "]";
	}

	@Override
	public String displayFull() {
		// TODO
		return "TODO";
	}
}
