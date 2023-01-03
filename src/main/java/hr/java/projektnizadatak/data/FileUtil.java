package hr.java.projektnizadatak.data;

import hr.java.projektnizadatak.shared.exceptions.InvalidDataException;
import hr.java.projektnizadatak.shared.exceptions.InvalidUsernameException;
import hr.java.projektnizadatak.shared.exceptions.ReadOrWriteErrorException;
import hr.java.projektnizadatak.shared.exceptions.UnreachableCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static void ensureFileExists(Path path) {
		try {
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			String m = "Creating file: " + path;
			logger.error(m);

			throw new ReadOrWriteErrorException(m, e);
		}
	}
}
