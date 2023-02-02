package hr.java.projektnizadatak.presentation.models;

import hr.java.projektnizadatak.application.UserManager;
import hr.java.projektnizadatak.application.entities.UserRole;
import hr.java.projektnizadatak.presentation.Application;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;
import hr.java.projektnizadatak.shared.exceptions.NoAdminUserException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageUsersModel {
	private static final Logger logger = LoggerFactory.getLogger(ManageUsersModel.class);
	
	private final ObservableList<UserItemModel> userList = FXCollections.observableArrayList();
	private UserItemModel selected;

	public void initialize() {
		var users = Application.getUserManager()
			.getAllUsers()
			.stream()
			.map(UserItemModel::new)
			.toList();

		userList.setAll(users);
	}

	public ObservableList<UserItemModel> getUserList() {
		return userList;
	}

	public UserItemModel getSelected() {
		return selected;
	}

	public void setSelected(UserItemModel selected) {
		this.selected = selected;
	}

	public void saveChanges() throws DataStoreException, NoAdminUserException {
		if (userList.stream().noneMatch(u -> u.getRole() == UserRole.ADMIN)) {
			String m = "At least one user has to be an admin";
			logger.info(m);
			
			throw new NoAdminUserException(m);
		}
		
		var users = userList.stream()
			.map(UserItemModel::toUser)
			.toList();
		
		Application.getUserManager().overrideUsers(users);
	}

	public void deleteSelected() {
		userList.remove(selected);
	}
}
