package hr.java.projektnizadatak.application.entities;

import hr.java.projektnizadatak.shared.Util;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

public record ScheduleOverride(ScheduleItem original, List<OverrideData> replacements) implements Serializable, Recordable {
	public List<ScheduleItem> getOverridden() {
		return replacements.stream()
			.map(replacement -> new ScheduleItem(
				original.originalId(),
				replacement.id(),
				original.courseName(),
				original.className(),
				Util.unlessNull(replacement.professor(), original.professor()),
				Util.unlessNull(replacement.classType(), original.classType()),
				Util.unlessNull(replacement.classroom(), original.classroom()),
				Util.unlessNull(replacement.note(), original.note()),
				Util.unlessNull(replacement.group(), original.group()),
				original.weekday(),
				Util.unlessNull(replacement.start(), original.start()),
				Util.unlessNull(replacement.end(), original.end()),
				false
			)).toList();
	}
	
	public ScheduleOverride withReplacements(List<OverrideData> replacements) {
		return new ScheduleOverride(original, replacements);
	}

	public static Timetable applyOverrides(Timetable timetable, List<ScheduleOverride> overrides) {
		var mappedItems = timetable.scheduleItems()
			.stream()
			.flatMap(original -> {
				var foundOverride = overrides.stream()
					.filter(ov -> ov.original().matches(original))
					.findFirst();

				if (foundOverride.isPresent()) {
					return foundOverride.get().getOverridden().stream();
				} else {
					return Stream.of(original);
				}
			})
			.toList();

		return timetable.withScheduleItems(mappedItems);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}

		ScheduleOverride that = (ScheduleOverride) o;

		if (!original.equals(that.original)) {return false;}
		return replacements.equals(that.replacements);
	}

	@Override
	public String displayShort() {
		return "%s: %s (%s %s) -> [%d]".formatted(
			original.courseName(),
			original.className(),
			Util.dayOfWeekToShortString(original.weekday()),
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
