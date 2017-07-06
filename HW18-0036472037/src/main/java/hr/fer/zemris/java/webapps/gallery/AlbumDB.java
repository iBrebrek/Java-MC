package hr.fer.zemris.java.webapps.gallery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Static class which simulates database.
 * Once {@link #createAlbum(String)} is
 * called that album is remembered and
 * calling {@link #getAlbum()} will
 * return that remembered album.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
public final class AlbumDB {
	/** Remembered album (last created album). */
	private static Album album;
	
	/**
	 * Retrieves last created album.
	 * 
	 * @return album with all pictures.
	 */
	public static Album getAlbum() {
		return album;
	}

	/**
	 * Creates and retrieves album with pictures
	 * described in given file.
	 * After calling this method created album
	 * can be simply retrieved by calling
	 * {@link #getAlbum()}.
	 * 
	 * @param picsDescriptor	file were pictures are described.
	 * @return created album.
	 */
	public static Album createAlbum(String picsDescriptor) {
		Path descriptor = Paths.get(picsDescriptor);
		album = new Album();
		try {
			int flag = 0;
			String source = null;
			String name = null;
			for (String line : Files.readAllLines(descriptor)) {
				if (line == null) {
					continue;
				}
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				switch (flag) {
				case 0:
					source = line;
					break;
				case 1:
					name = line;
					break;
				case 2:
					String[] tags = line.split(",");
					album.addPicture(source, name, tags);
					break;
				default:
					// should never happen
					throw new RuntimeException("Unexpected error.");
				}
				flag = (flag + 1) % 3;
			}
		} catch (IOException exc) {
			throw new RuntimeException("Unable to read from given paths.");
		}
		return album;
	}
}
