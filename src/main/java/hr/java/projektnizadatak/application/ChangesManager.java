package hr.java.projektnizadatak.application;

import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.data.ChangesFileStore;

import java.util.List;

class ChangesManager {
	private static ChangesManager instance = null;

	private final ChangesStore changesStore;

	private ChangesManager(ChangesStore changesStore) {
		this.changesStore = changesStore;
	}

	public static ChangesManager getInstance() {
		if (instance == null) {
			instance = new ChangesManager(new ChangesFileStore());
		}

		return instance;
	}

	public List<Change> getAllChanges() {
		return changesStore.read();
	}

	public void addChange(Change change) {
		changesStore.create(change);
	}
}
