package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.UserStore;
import hr.java.projektnizadatak.application.entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class UserFileStore implements UserStore {
	private final String USERS_FILE_PATH = "data/users.txt";

	@Override
	public List<User> loadUsers() {
		try {
			return Files.readAllLines(Path.of(USERS_FILE_PATH))
				.stream()
				.map(this::parseUser)
				.toList();
		} catch (IOException e) {
			// TODO: implement proper error handling
			throw new RuntimeException(e);
		}
	}

	private User parseUser(String line) {
		var s = line.split(":");
		
		return new User.UserBuilder(s[0])
			.withPasswordHash(s[1])
			.build();
	}

	@Override
	public void storeUsers(List<User> users) {
		var serialized = users.stream()
			.map(this::formatUser)
			.collect(Collectors.joining());

		try {
			Files.writeString(Path.of(USERS_FILE_PATH), serialized);
		} catch (IOException e) {
			// TODO: implement proper error handling
			throw new RuntimeException(e);
		}
	}

	private String formatUser(User user) {
		return user.username() + ':' + user.passwordHash() + '\n';
	}
}
