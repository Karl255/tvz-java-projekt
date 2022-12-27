package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.shared.exceptions.InvalidUsernameException;
import hr.java.projektnizadatak.shared.exceptions.UsernameTakenException;

import java.util.ArrayList;

public class UserManager {
	private final UserStore userStore;
	private User loggedInUser = null;

	public UserManager(UserStore userStore) {
		this.userStore = userStore;
	}

	public boolean tryLoginUser(String username, String password) {
		var passwordHash = User.hashPassword(password);
		var users = userStore.loadUsers();

		var found = users.stream()
			.filter(u -> u.username().equals(username) && u.passwordHash().equals(passwordHash))
			.findFirst();

		if (found.isPresent()) {
			loggedInUser = found.get();
		}

		return found.isPresent();
	}

	public void logout() {
		loggedInUser = null;
	}

	public User createUser(String username, String password) throws InvalidUsernameException, UsernameTakenException {
		if (!User.isUsernameValid(username)) {
			throw new InvalidUsernameException("Invalid username: " + username);
		}

		var passwordHash = User.hashPassword(password);
		var users = new ArrayList<>(userStore.loadUsers());

		boolean usernameTaken = users.stream()
			.anyMatch(u -> u.username().equals(username));

		if (usernameTaken) {
			throw new UsernameTakenException("Username taken: " + username);
		}

		var user = new User.UserBuilder(username)
			.withPasswordHash(passwordHash)
			.build();
		
		users.add(user);
		userStore.storeUsers(users);
		
		return user;
	}

	public User getUser() {
		return loggedInUser;
	}
}
