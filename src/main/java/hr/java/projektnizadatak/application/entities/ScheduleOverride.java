package hr.java.projektnizadatak.application.entities;

import hr.java.projektnizadatak.shared.Util;

import java.io.Serializable;
import java.time.DayOfWeek;
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
		return "%s: %s (%s %s) -> [%d]".formatted(
			original.courseName(),
			original.className(),
			Util.dayOfWeekToShortString(original.date().getDayOfWeek()),
			original.getTimestampFull(),
			replacements.size()
		);
	}

	@Override
	public String displayFull() {
		var sb = new StringBuilder(displayShort())
			.append('\n');
		
		for (var replacement : replacements) {
			var s = "[%s] %s u %s sa %s; %s; %s\n".formatted(
				replacement.getTimestampFull(),
				replacement.classType().toShortString(),
				replacement.classroom(),
				replacement.professor(),
				Util.unlessNull(replacement.group(), "-"),
				Util.unlessNull(replacement.note(), "-")
			);
			
			sb.append(s);
		}
		
		return sb.toString();
	}
}
