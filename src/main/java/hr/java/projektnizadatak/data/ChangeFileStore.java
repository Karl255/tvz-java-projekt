package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ChangeStore;
import hr.java.projektnizadatak.application.entities.Change;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeFileStore implements ChangeStore {
	private static final String CHANGES_FILE_PATH = "data/changes.dat";

	@Override
	public void create(Change change) {
		try (var out = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE_PATH, true))) {
			out.writeObject(change);
		} catch (IOException e) {
			// TODO: handle each case differently with custom exceptions
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Change> read() {
		var changes = new ArrayList<Change>();

		try (var in = new ObjectInputStream(new FileInputStream(CHANGES_FILE_PATH))) {
			while (in.available() > 0) {
				changes.add((Change) in.readObject());
			}

			return changes;
		} catch (IOException | ClassNotFoundException e) {
			// TODO: handle each case differently with custom exceptions
			throw new RuntimeException(e);
		}
	}
}
