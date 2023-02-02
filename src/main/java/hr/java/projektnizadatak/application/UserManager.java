package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.*;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.exceptions.InvalidUsernameException;
import hr.java.projektnizadatak.shared.exceptions.UsernameTakenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserManager {
	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	private final UsersStore usersStore;
	private String loggedInUsername = null;
	private User loggedInUserFallback = null;

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
			var user = found.get();
			loggedInUsername = user.username();
			loggedInUserFallback = user;
		}

		return found.isPresent();
	}

	public void logout() {
		loggedInUsername = null;
	}

	// create
	
	public User createUser(String username, String password) throws InvalidUsernameException, UsernameTakenException {
		if (!User.isUsernameValid(username)) {
			String m = "Invalid username: " + username;
			logger.error(m);
			
			throw new InvalidUsernameException(m);
		}

		var users = usersStore.read();

		boolean usernameTaken = users.stream()
			.anyMatch(u -> u.username().equals(username));

		if (usernameTaken) {
			String m = "Username taken: " + username;
			logger.error(m);
			
			throw new UsernameTakenException(m);
		}

		var user = new User.UserBuilder(username, users.size() > 0 ? UserRole.USER : UserRole.ADMIN)
			.withPassword(password)
			.build();

		usersStore.create(user);

		return user;
	}

	// read

	public User getLoggedInUser() {
		return usersStore.read().stream()
			.filter(u -> u.username().equals(loggedInUsername))
			.findFirst()
			.orElse(loggedInUserFallback);
	}
	
	public List<User> getAllUsers() {
		return usersStore.read();
	}

	// update
	
	public User updateLoggedInSettings(String departmentCode, Semester semester) {
		return updateUserSettings(getLoggedInUser(), departmentCode, semester);
	}
	
	public User updateUserSettings(User user, String departmentCode, Semester semester) {
		var newUser = new User.UserBuilder(user)
			.withDefaultDepartmentCode(departmentCode)
			.withDefaultSemester(semester)
			.build();

		usersStore.update(user, newUser);
		
		return newUser;
	}

	public void overrideUsers(List<User> users) {
		if (loggedInUserFallback.role() == UserRole.ADMIN) {
			usersStore.overrideAll(users);
		}
	}
	
	// delete
}
