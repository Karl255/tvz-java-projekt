package hr.java.projektnizadatak.application.entities;

import java.util.List;

public record ScheduleOverride(ScheduleItem original, List<ScheduleItem> replacements) {
	@Override
	public boolean equals(Object o) {
		if (this == o) {return true;}
		if (o == null || getClass() != o.getClass()) {return false;}

		ScheduleOverride that = (ScheduleOverride) o;

		if (!original.equals(that.original)) {return false;}
		return replacements.equals(that.replacements);
	}
}
