package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.util.List;

public record ScheduleOverride(ScheduleItem original, List<ScheduleItem> replacements) implements Serializable {
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

	public ScheduleOverride updateReplacement(ScheduleItem oldReplacement, ScheduleItem newReplacement) {
		var newReplacements = replacements.stream()
			.map(r -> r.equals(oldReplacement)
				? newReplacement
				: oldReplacement)
			.toList();

		return new ScheduleOverride(original, newReplacements);
	}
}
