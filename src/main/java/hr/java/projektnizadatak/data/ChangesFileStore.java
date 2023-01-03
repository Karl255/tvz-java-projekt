package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ChangesStore;
import hr.java.projektnizadatak.application.entities.Change;

import java.nio.file.Path;

public class ChangesFileStore extends ObjectStore<Change> implements ChangesStore {
	private static final Path CHANGES_FILE_PATH = Path.of("data/changes.dat");

	public ChangesFileStore() {
		super(CHANGES_FILE_PATH, Change.class);
	}
}
