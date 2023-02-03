package hr.java.projektnizadatak.presentation.controllers;

import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.presentation.models.UserModel;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.NoAdminUserException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageUsersController {
	private static final Logger logger = LoggerFactory.getLogger(ManageUsersController.class);
	
	private final ObservableList<UserModel> userList = FXCollections.observableArrayList();
	private UserModel selected;

	public void initialize() {
		var users = Application.getUserManager()
			.getAllUsers()
			.stream()
			.map(UserModel::new)
			.toList();

		userList.setAll(users);
	}

	public ObservableList<UserModel> getUserList() {
		return userList;
	}

	public UserModel getSelected() {
		return selected;
	}

	public void setSelected(UserModel selected) {
		this.selected = selected;
	}

	public void saveChanges() throws DataStoreException, NoAdminUserException {
		if (userList.stream().noneMatch(u -> u.getRole() == UserRole.ADMIN)) {
			String m = "At least one user has to be an admin";
			logger.info(m);
			
			throw new NoAdminUserException(m);
		}
		
		var users = userList.stream()
			.map(UserModel::toUser)
			.toList();
		
		Application.getUserManager().overrideUsers(users);
	}

	public void deleteSelected() {
		userList.remove(selected);
	}
}
