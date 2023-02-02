package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ChangesManager;
import hr.java.projektnizadatak.application.UsersStore;
import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsersFileStore implements UsersStore {
	private static final Logger logger = LoggerFactory.getLogger(UsersFileStore.class);
	private static final Path USERS_FILE_PATH = Path.of("data/users.txt");

	public UsersFileStore() {
		FileUtil.ensureFileExists(USERS_FILE_PATH);
	}

	@Override
	public void create(User user) {
		var users = read();
		users.add(user);

		storeAll(users);
		logChange(null, user);
	}

	@Override
	public List<User> read() {
		try {
			return Files.readAllLines(USERS_FILE_PATH)
				.stream()
				.map(this::parseUser)
				.collect(Collectors.toCollection(ArrayList::new));
		} catch (IOException e) {
			String m = "Error while reading file: " + USERS_FILE_PATH;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		}
	}

	@Override
	public void update(User oldUser, User newUser) {
		var users = read();

		storeAll(users.stream()
			.map(u -> u.equals(oldUser) ? newUser : u)
			.toList()
		);
		
		logChange(oldUser, newUser);
	}

	@Override
	public void overrideAll(List<User> users) {
		var oldUsers = read();
		storeAll(users);
		
		// new users
		users.stream()
			.filter(u -> oldUsers.stream().noneMatch(ou -> ou.username().equals(u.username())))
			.forEach(u -> logChange(null, u));
		
		// modified and deleted users
		for (var oldUser : oldUsers) {
			var modifiedUser = users.stream()
				.filter(u -> u.username().equals(oldUser.username()))
				.findFirst()
				.orElse(null);
			
			if (!oldUser.equals(modifiedUser)) {
				logChange(oldUser, modifiedUser);
			}
		}
	}

	private User parseUser(String line) {
		var s = line.split(":", -1);
		var ub = new User.UserBuilder(s[0], UserRole.parse(s[2]))
			.withPasswordHash(s[1]);

		if (!s[3].equals("")) {
			ub.withDefaultDepartmentCode(s[3]);
		}

		if (!s[4].equals("")) {
			ub.withDefaultSemester(parseSemester(s[4]));
		}

		return ub.build();
	}

	private Semester parseSemester(String string) {
		var splits = string.split("-");
		int semesterNumber = Integer.parseInt(splits[0]);

		return new Semester(splits[1], semesterNumber);
	}

	private void storeAll(List<User> users) {
		var serialized = users.stream()
			.map(this::formatUser)
			.collect(Collectors.joining());

		try {
			Files.writeString(USERS_FILE_PATH, serialized);
		} catch (IOException e) {
			String m = "Error while writing file: " + USERS_FILE_PATH;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		}
	}

	private String formatUser(User user) {
		return new StringBuilder()
			.append(user.username())
			.append(':')
			.append(user.passwordHash())
			.append(':')
			.append(user.role().getName())
			.append(':')
			.append(user.defaultDepartmentCode() != null ? user.defaultDepartmentCode() : "")
			.append(':')
			.append(semesterToString(user.defaultSemester()))
			.append('\n')
			.toString();
	}

	private String semesterToString(Semester semester) {
		return semester != null
			? semester.semester() + "-" + semester.subdepartment()
			: "";
	}

	// logging

	private void logChange(User oldValue, User newValue) {
		var user = Application.getUserManager().getLoggedInUser();
		var change = Change.create(user, oldValue, newValue);
		ChangesManager.getInstance().addChange(change);
	}
}
