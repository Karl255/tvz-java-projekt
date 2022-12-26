package hr.java.projektnizadatak.application.entities;

import java.util.regex.Pattern;

public record User(String username, String passwordHash) {
	private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\-_ ]{6,32}$");
	
	public boolean isUsernameValid() {
		return VALID_USERNAME_PATTERN.matcher(username).matches();
	}
}
