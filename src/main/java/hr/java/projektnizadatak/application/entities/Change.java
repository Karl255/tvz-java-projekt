package hr.java.projektnizadatak.application.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Change(String username, LocalDateTime timestamp, String oldValue, String newValue) implements Serializable {
	public static <T> Change create(User user, T oldValue, T newValue) {
		return new Change(user.username(), LocalDateTime.now(), oldValue.toString(), newValue.toString());
	}
}
