package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.util.Arrays;

public enum ClassType implements Serializable {
	LECTURES("Predavanja"),
	AUDITORY_EXERCISES("Auditorne vježbe"),
	LAB("Laboratorijske vježbe"),
	SPORTS_ACTIVITY("Tjelesna aktivnost"),
	OTHER("Ostalo"),
	;

	private final String name;

	ClassType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static ClassType parse(String string) {
		return Arrays.stream(values())
			.filter(t -> t.getName().equals(string))
			.findFirst()
			.orElse(ClassType.OTHER);
	}
	
	public String toShortString() {
		return switch (name) {
			case "Predavanja" -> "Pred.";
			case "Auditorne vježbe" -> "AV";
			case "Laboratorijske vježbe" -> "LV";
			case "Ostalo" -> "Ostalo";
			default -> "?";
		};
	}
}
