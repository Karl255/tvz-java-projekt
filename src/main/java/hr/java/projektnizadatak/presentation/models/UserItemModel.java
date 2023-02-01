package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.entities.Semester;
import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.application.entities.UserRole;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserItemModel {
	private final ReadOnlyStringProperty username;
	private final ReadOnlyStringProperty passwordHash;
	private final SimpleObjectProperty<UserRole> role;
	private final ReadOnlyStringProperty defaultDepartmentCode;
	private final ReadOnlyObjectProperty<Semester> defaultSemester;

	public UserItemModel(User user) {
		username = new SimpleStringProperty(user.username());
		passwordHash = new SimpleStringProperty(user.passwordHash());
		role = new SimpleObjectProperty<>(user.role());
		defaultDepartmentCode = new SimpleStringProperty(user.defaultDepartmentCode());
		defaultSemester = new SimpleObjectProperty<>(user.defaultSemester());
	}
	
	public User toUser() {
		return new User.UserBuilder(username.get(), role.get())
			.withPasswordHash(passwordHash.get())
			.withDefaultDepartmentCode(defaultDepartmentCode.get())
			.withDefaultSemester(defaultSemester.get())
			.build();
	}

	public String getUsername() {
		return username.get();
	}

	public ReadOnlyStringProperty usernameProperty() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash.get();
	}

	public ReadOnlyStringProperty passwordHashProperty() {
		return passwordHash;
	}

	public UserRole getRole() {
		return role.get();
	}

	public SimpleObjectProperty<UserRole> roleProperty() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role.set(role);
	}

	public String getDefaultDepartmentCode() {
		return defaultDepartmentCode.get();
	}

	public ReadOnlyStringProperty defaultDepartmentCodeProperty() {
		return defaultDepartmentCode;
	}

	public Semester getDefaultSemester() {
		return defaultSemester.get();
	}

	public ReadOnlyObjectProperty<Semester> defaultSemesterProperty() {
		return defaultSemester;
	}
}
