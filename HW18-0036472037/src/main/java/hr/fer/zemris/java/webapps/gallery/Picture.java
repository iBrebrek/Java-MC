package hr.fer.zemris.java.webapps.gallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Instances of this class are
 * representations of single picture.
 * Each picture has name, source 
 * (file name) and list of tags.
 * Once created, picture properties 
 * can not be changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
public class Picture {
	/** File name in which this picture is stored. */
	private final String source;
	/** Name of this picture. */
	private final String name;
	/** All tags of this picture. */
	private final List<String> tags = new ArrayList<>();
	
	/**
	 * Initializes new picture.
	 * None of given parameters can be null.
	 * 
	 * @param source	file name of this picture.
	 * @param name		name of this picture.
	 * @param tags		all tags for this picture.
	 */
	public Picture(String source, String name, String... tags) {
		if(source == null || name == null) {
			throw new IllegalArgumentException("Picture must have a name and location on disk.");
		}
		this.source = source;
		this.name = name;
		for(String tag : tags) {
			if(tag == null) {
				continue;
			}
			tag = tag.toLowerCase().trim();
			this.tags.add(tag);
		}
	}

	/**
	 * Retrieves source of this
	 * picture.
	 * 
	 * @return picture source.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Retrieves picture name.
	 * 
	 * @return picture name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves list of all tags.
	 * List is unmodifiable.
	 * 
	 * @return list of tags.
	 */
	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}
}
