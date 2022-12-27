package hr.java.projektnizadatak.application;

import java.util.List;

import hr.java.projektnizadatak.application.entities.User;

public interface UserStore {
	// use CRUD instead
	
	List<User> loadUsers();

	void storeUsers(List<User> users);
}
