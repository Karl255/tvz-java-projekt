package hr.java.projektnizadatak.application.entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public final class User {
	private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\-_ ]{4,32}$");
	
	private final String username;
	private final String passwordHash;
	private final String defaultDepartmentCode; 
	private final Semester defaultSemester;

	private User(String username, String passwordHash, String defaultDepartmentCode, Semester defaultSemester) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.defaultDepartmentCode = defaultDepartmentCode;
		this.defaultSemester = defaultSemester;
	}

	public static String hashPassword(String password) {
		try {
			var digest = MessageDigest.getInstance("SHA-256")
				.digest(password.getBytes());
			
			return new String(Base64.getEncoder()
				.encode(digest));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isUsernameValid(String username) {
		return VALID_USERNAME_PATTERN.matcher(username)
			.matches();
	}

	public String username() {return username;}

	public String passwordHash() {return passwordHash;}

	public String defaultDepartmentCode() {return defaultDepartmentCode;}

	public Semester defaultSemester() {return defaultSemester;}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {return true;}
		if (obj == null || obj.getClass() != this.getClass()) {return false;}
		var that = (User) obj;
		return Objects.equals(this.username, that.username) && Objects.equals(this.passwordHash, that.passwordHash);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, passwordHash);
	}

	@Override
	public String toString() {
		return "User{" +
			"username=" + username +
			", passwordHash=" + passwordHash +
			", defaultDepartmentCode=" + defaultDepartmentCode +
			", defaultSemester=" + defaultSemester +
			'}';
	}

	public static class UserBuilder {
		private final String username;
		private String passwordHash;
		private String defaultDepartmentCode = null;
		private Semester defaultSemester = null;

		public UserBuilder(String username) {
			this.username = username;
		}

		public UserBuilder withPasswordHash(String passwordHash) {
			this.passwordHash = passwordHash;
			return this;
		}

		public UserBuilder withPassword(String password) {
			this.passwordHash = hashPassword(password);
			return this;
		}
		
		public UserBuilder withDefaultDepartmentCode(String defaultDepartmentCode) {
			this.defaultDepartmentCode = defaultDepartmentCode;
			return this;
		}
		
		public UserBuilder withDefaultSemester(Semester defaultSemester) {
			this.defaultSemester = defaultSemester;
			return this;
		}

		public User build() {
			return new User(username, passwordHash, defaultDepartmentCode, defaultSemester);
		}
	}
}
