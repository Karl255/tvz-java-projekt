package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;

import java.util.List;

public class ChangeManager {
	private final ChangeStore changeStore;

	public ChangeManager(ChangeStore changeStore) {
		this.changeStore = changeStore;
	}
	
	public List<Change> getAllChanges() {
		// TODO
		return null;
	}
	
	public void addChange(Change change) {
		// TODO
	}
}
