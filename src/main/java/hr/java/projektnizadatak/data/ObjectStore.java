package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.InvalidDataException;
import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class ObjectStore<T extends Serializable> {
	private static final Logger logger = LoggerFactory.getLogger(ObjectStore.class);
	private final Path path;
	private final Class<T> type;

	public ObjectStore(Path path, Class<T> type) {
		this.path = path;
		this.type = type;
	}

	public void create(T item) {
		var items = new ArrayList<>(read());
		items.add(item);
		writeAll(items);
	}

	public List<T> read() {
		FileUtil.ensureFileExists(path);
		var items = new ArrayList<T>();

		try (var in = new ObjectInputStream(new FileInputStream(path.toString()))) {
			while (true) {
				items.add(type.cast(in.readObject()));
			}
		} catch (EOFException e) {
			String m = String.format("Reached end of objects file after reading %d objects: %s", items.size(), path);
			logger.info(m);
			
			return items;
		} catch (FileNotFoundException e) {
			String m = "File not found while reading entries: " + path;
			logger.error(m);

			throw new UnreachableCodeException(m, e);
		} catch (IOException e) {
			String m = "Error while reading file: " + path;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		} catch (ClassNotFoundException e) {
			String m = "Found unknown type in objects file, file is likely corrupted: " + path;
			logger.error(m);

			throw new InvalidDataException(m, e);
		} catch (ClassCastException e) {
			String m = "Found unexpected type in objects file, file is likely corrupted: " + path;
			logger.error(m);

			throw new InvalidDataException(m, e);
		}
	}

	public void update(T oldItem, T newItem) {
		writeAll(read()
			.stream()
			.map(i -> i.equals(oldItem) ? newItem : i)
			.toList()
		);
	}

	public void delete(T item) {
		writeAll(read()
			.stream()
			.filter(i -> !i.equals(item))
			.toList()
		);
	}

	private void writeAll(List<T> items) {
		FileUtil.ensureFileExists(path);

		try (var out = new ObjectOutputStream(new FileOutputStream(path.toString(), false))) {
			for (var item : items) {
				out.writeObject(item);
			}
		} catch (FileNotFoundException e) {
			String m = "File not accessible: " + path;
			logger.error(m);

			throw new UnreachableCodeException(m, e);
		} catch (IOException e) {
			String m = "Error while writing file: " + path;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		}
	}
}
