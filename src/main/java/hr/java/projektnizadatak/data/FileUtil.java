package hr.java.projektnizadatak.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class FileUtil {
	public static void ensureFileExists(Path path) {
		try {
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
