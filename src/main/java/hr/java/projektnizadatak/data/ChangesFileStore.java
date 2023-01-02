package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ChangesStore;
import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.shared.exceptions.InvalidDataException;
import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChangesFileStore implements ChangesStore {
	private static final Path CHANGES_FILE_PATH = Path.of("data/changes.dat");

	@Override
	public void create(Change change) {
		FileUtil.ensureFileExists(CHANGES_FILE_PATH);
		
		try (var out = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE_PATH.toString(), true))) {
			out.writeObject(change);
		} catch (FileNotFoundException e) {
			throw new UnreachableCodeException("File not found while creating change entry", e);
		} catch (IOException e) {
			throw new ReadOrWriteErrorException("Writing file: " + CHANGES_FILE_PATH, e);
		}
	}

	@Override
	public List<Change> read() {
		FileUtil.ensureFileExists(CHANGES_FILE_PATH);
		
		var changes = new ArrayList<Change>();

		try (var in = new ObjectInputStream(new FileInputStream(CHANGES_FILE_PATH.toString()))) {
			while (in.available() > 0) {
				changes.add((Change) in.readObject());
			}

			return changes;
		} catch (FileNotFoundException e) {
			throw new UnreachableCodeException("File not found while reading change entries", e);
		} catch (IOException e) {
			throw new ReadOrWriteErrorException("Reading file: " + CHANGES_FILE_PATH, e);
		} catch (ClassNotFoundException e) {
			throw new InvalidDataException("File: " + CHANGES_FILE_PATH, e);
		}
	}
}
