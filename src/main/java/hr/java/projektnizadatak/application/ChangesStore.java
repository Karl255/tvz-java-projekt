package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;

import java.util.List;

public interface ChangesStore {
	void create(Change change);
	List<Change> read();
	// no update
	// no delete
}
