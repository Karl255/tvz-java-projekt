package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.shared.exceptions.InvalidUsernameException;
import hr.java.projektnizadatak.shared.exceptions.UsernameTakenException;

public class UserManager {
	private final UsersStore usersStore;
	private User loggedInUser = null;

	public UserManager(UsersStore usersStore) {
		this.usersStore = usersStore;
	}

	public boolean tryLoginUser(String username, String password) {
		var passwordHash = User.hashPassword(password);
		var users = usersStore.read();

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

		var users = usersStore.read();

		boolean usernameTaken = users.stream()
			.anyMatch(u -> u.username().equals(username));

		if (usernameTaken) {
			throw new UsernameTakenException("Username taken: " + username);
		}

		var user = new User.UserBuilder(username)
			.withPassword(password)
			.build();

		usersStore.create(user);

		return user;
	}

	public User updateLoggedInSettings(String departmentCode, Semester semester) {
		return updateUserSettings(loggedInUser, departmentCode, semester);
	}
	
	public User updateUserSettings(User user, String departmentCode, Semester semester) {
		var newUser = new User.UserBuilder(user)
			.withDefaultDepartmentCode(departmentCode)
			.withDefaultSemester(semester)
			.build();

		usersStore.update(user, newUser);
		
		return newUser;
	}

	public User getUser() {
		return loggedInUser;
	}
}
