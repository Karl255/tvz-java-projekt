package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.UserStore;
import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class UserFileStore implements UserStore {
	private static final Path USERS_FILE_PATH = Path.of("data/users.txt");

	public UserFileStore() {
		FileUtil.ensureFileExists(USERS_FILE_PATH);
	}

	@Override
	public List<User> loadUsers() {
		try {
			return Files.readAllLines(USERS_FILE_PATH)
				.stream()
				.map(this::parseUser)
				.toList();
		} catch (IOException e) {
			// TODO: implement proper error handling
			throw new RuntimeException(e);
		}
	}

	private User parseUser(String line) {
		var s = line.split(":", -1);
		var ub = new User.UserBuilder(s[0])
			.withPasswordHash(s[1]);
		
		if (!s[2].equals("")) {
			ub.withDefaultDepartmentCode(s[2]);
		}
		
		if (!s[3].equals("")) {
			ub.withDefaultSemester(parseSemester(s[3]));
		}
		
		return ub.build();
	}

	private Semester parseSemester(String string) {
		var splits = string.split("-");
		int semesterNumber = Integer.parseInt(splits[0]);
		
		return new Semester(splits[1], semesterNumber);
	}
	
	@Override
	public void storeUsers(List<User> users) {
		var serialized = users.stream()
			.map(this::formatUser)
			.collect(Collectors.joining());

		try {
			Files.writeString(USERS_FILE_PATH, serialized);
		} catch (IOException e) {
			// TODO: implement proper error handling
			throw new RuntimeException(e);
		}
	}

	private String formatUser(User user) {
		return new StringBuilder()
			.append(user.username())
			.append(':')
			.append(user.passwordHash())
			.append(':')
			.append(user.defaultDepartmentCode() != null ? user.defaultDepartmentCode() : "")
			.append(':')
			.append(semesterToString(user.defaultSemester()))
			.append('\n')
			.toString();
	}
	
	private String semesterToString(Semester semester) {
		return semester != null
			? semester.semester() + '-' + semester.subdepartment()
			: "";
	}
}
