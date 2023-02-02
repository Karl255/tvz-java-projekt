package hr.java.projektnizadatak.application.entities;

import hr.java.projektnizadatak.shared.Util;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Change(String username, LocalDateTime timestamp, Recordable oldValue, Recordable newValue) implements Serializable {
	public static Change create(User user, Recordable oldValue, Recordable newValue) {
		return new Change(
			Util.nullCoalesc(user, User::username),
			LocalDateTime.now(),
			oldValue,
			newValue
		);
	}
}
