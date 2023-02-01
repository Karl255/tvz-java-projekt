package hr.java.projektnizadatak.application.entities;

import java.util.Arrays;

public enum UserRole {
	ADMIN("ADMIN"),
	USER("USER");
	
	private final String name;

	UserRole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static UserRole parse(String string) {
		return Arrays.stream(values())
			.filter(t -> t.getName().equals(string))
			.findFirst()
			.orElse(USER);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
