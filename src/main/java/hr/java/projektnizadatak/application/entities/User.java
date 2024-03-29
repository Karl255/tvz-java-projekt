package hr.java.projektnizadatak.application.entities;

import hr.java.projektnizadatak.shared.exceptions.UnsupportedAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public final class User implements Serializable, Recordable {
	private static final Logger logger = LoggerFactory.getLogger(User.class);

	private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9\\-_ ]{4,32}$");

	private final String username;
	private final String passwordHash;
	private final UserRole role;
	private final String defaultDepartmentCode;
	private final Semester defaultSemester;

	private User(String username, String passwordHash, UserRole role, String defaultDepartmentCode, Semester defaultSemester) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.role = role;
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
			String m = "System doesn't support SHA-256";
			logger.error(m);

			throw new UnsupportedAlgorithmException(m, e);
		}
	}

	public static boolean isUsernameValid(String username) {
		return VALID_USERNAME_PATTERN.matcher(username)
			.matches();
	}

	public String username() {return username;}

	public String passwordHash() {return passwordHash;}

	public UserRole role() {return role;}

	public String defaultDepartmentCode() {return defaultDepartmentCode;}

	public Semester defaultSemester() {return defaultSemester;}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {return true;}
		if (obj == null || obj.getClass() != this.getClass()) {return false;}
		var that = (User) obj;
		return role == that.role && Objects.equals(this.username, that.username) && Objects.equals(this.passwordHash, that.passwordHash);
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

	@Override
	public String displayShort() {
		var sb = new StringBuilder(username)
			.append(" (").append(role.getName()).append("); ");

		if (defaultDepartmentCode != null) {
			sb.append(defaultDepartmentCode);
		}
		
		sb.append("; ");

		if (defaultSemester != null) {
			sb.append(defaultSemester.semester())
				.append("-")
				.append(defaultSemester.subdepartment());
		}

		return sb.toString();
	}

	@Override
	public String displayFull() {
		return displayShort() + "; " + passwordHash;
	}

	public static class UserBuilder {
		private final String username;
		private String passwordHash;
		private final UserRole role;
		private String defaultDepartmentCode = null;
		private Semester defaultSemester = null;

		public UserBuilder(String username, UserRole role) {
			this.username = username;
			this.role = role;
		}

		public UserBuilder(User original) {
			this.username = original.username;
			this.passwordHash = original.passwordHash;
			this.role = original.role;
			this.defaultDepartmentCode = original.defaultDepartmentCode;
			this.defaultSemester = original.defaultSemester;
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
			return new User(username, passwordHash, role, defaultDepartmentCode, defaultSemester);
		}
	}
}
