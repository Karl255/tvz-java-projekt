package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.User;

import java.util.ArrayList;

public class UserManager {
	private final UserStore userStore;
	private User loggedInUser = null;

	public UserManager(UserStore userStore) {
		this.userStore = userStore;
	}

	public boolean tryLoginUser(User user) {
		var users = userStore.loadUsers();

		var found = users.stream()
			.anyMatch(user::equals);

		if (found) {
			loggedInUser = user;
		}

		return found;
	}

	public void logout() {
		loggedInUser = null;
	}

	public boolean tryAddUser(User user) {
		if (!user.isUsernameValid()) {
			return false;
		}
		
		var users = new ArrayList<>(userStore.loadUsers());
		boolean usernameTaken = users.stream()
			.anyMatch(u -> u.username().equals(user.username()));
		
		if (usernameTaken) {
			return false;
		}
		
		users.add(user);
		userStore.storeUsers(users);
		return true;
	}

	public User getUser() {
		return loggedInUser;
	}
}
