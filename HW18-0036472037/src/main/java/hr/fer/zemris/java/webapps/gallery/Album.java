package hr.fer.zemris.java.webapps.gallery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Instances of this class store pictures.
 * Pictures can be added but not deleted.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (11.7.2016.)
 */
public class Album {
	/** All pictures in this album. */
	private final List<Picture> pictures = new ArrayList<>();
	/** Pictures mapped by tags. */
	private final Map<String, List<Picture>> tags = new HashMap<>();

	/**
	 * Adds given picture to this album.
	 * 
	 * @param picture		picture that will be added to album.
	 */
	public void addPicture(Picture picture) {
		if(picture == null) {
			throw new IllegalArgumentException("Picture can not be null.");
		}
		pictures.add(picture);
		for(String tag : picture.getTags()) {
			if(!tags.containsKey(tag)) {
				tags.put(tag, new ArrayList<>());
			}
			List<Picture> list = tags.get(tag);
			list.add(picture);
		}
	}
	
	/**
	 * Creates picture with given properties 
	 * and adds it to this album.
	 * 
	 * @param source	file name of this picture.
	 * @param name		name of this picture.
	 * @param tags		all tags for this picture.
	 */
	public void addPicture(String source, String name, String... tags) {
		addPicture(new Picture(source, name, tags));
	}
	
	/**
	 * Retrieves set of all tags.
	 * Retrieved set can not be modified.
	 * 
	 * @return set of all tags.
	 */
	public Set<String> getTags() {
		return Collections.unmodifiableSet(tags.keySet());
	}
	
	/**
	 * Retrieves all pictures with given tag.
	 * Retrieved list can not be modified.
	 * 
	 * @param tag	picture tag.
	 * @return list of all pictures with given tag.
	 */
	public List<Picture> getPictures(String tag) {
		return Collections.unmodifiableList(tags.get(tag));
	}
	
	/**
	 * Retrieves picture with given name.
	 * 
	 * @param name		picture name.
	 * @return picture.
	 */
	public Picture getPicture(String name) {
		Picture result = null;
		for(Picture p : pictures) {
			if(p.getName().equals(name)) {
				result = p;
				break;
			}
		}
		return result;
	}
}
