package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.application.ChangesStore;
import hr.java.projektnizadatak.application.entities.Change;
import hr.java.projektnizadatak.shared.exceptions.InvalidDataException;
import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ChangesFileStore implements ChangesStore {
	private static final Logger logger = LoggerFactory.getLogger(ChangesFileStore.class);
	private static final Path CHANGES_FILE_PATH = Path.of("data/changes.dat");

	@Override
	public void create(Change change) {
		FileUtil.ensureFileExists(CHANGES_FILE_PATH);

		try (var out = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE_PATH.toString(), true))) {
			out.writeObject(change);
		} catch (FileNotFoundException e) {
			String m = "File not found while creating change entry";
			logger.error(m);

			throw new UnreachableCodeException(m, e);
		} catch (IOException e) {
			String m = "Writing file: " + CHANGES_FILE_PATH;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
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
			String m = "File not found while reading change entries";
			logger.error(m);

			throw new UnreachableCodeException(m, e);
		} catch (IOException e) {
			String m = "Reading file: " + CHANGES_FILE_PATH;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		} catch (ClassNotFoundException e) {
			String m = "File: " + CHANGES_FILE_PATH;
			logger.error(m);

			throw new InvalidDataException(m, e);
		}
	}
}
