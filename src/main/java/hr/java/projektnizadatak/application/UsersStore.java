package hr.java.projektnizadatak.application;

import java.util.List;

import hr.java.projektnizadatak.application.entities.User;

public interface UsersStore {
	void create(User user);
	
	List<User> read();

	void update(User oldUser, User newUser);
	
	// no delete (for now)
}
