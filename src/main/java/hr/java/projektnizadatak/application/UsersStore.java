package hr.java.projektnizadatak.application;

import java.util.List;

import hr.java.projektnizadatak.application.entities.User;
import hr.java.projektnizadatak.shared.exceptions.DataStoreException;

public interface UsersStore {
	void create(User user);
	
	List<User> read();

	void update(User oldUser, User newUser);
	
	// no delete (for now)
	
	void overrideAll(List<User> users) throws DataStoreException;
}
